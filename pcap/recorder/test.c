#include <stdio.h>
#include <pcap/pcap.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include "signal.h"

char *savePath=NULL;
char *storeSavePath=NULL;
#define PROGRAM_PID_FILE "/program.pid"
//fork
//--------------------------------
static void demonize( void )
{
	pid_t pid;


	pid = fork();
	if ( pid < 0 )
	{
		printf( "fork error\n" );
		exit( 0 );
	}

	if ( pid != 0 )
	{
		exit( 0 );
	}


	if ( setsid() < 0 )
	{
		exit( 0 );
	}

	umask( 0 );
	return;
}


/**************************************/
/****        防止进程启动两次       ***/
/**************************************/
int program_pid_write( char *pidfile, pid_t pid )
{
	FILE *file = fopen( pidfile, "w" );
	if ( !file )
	{
		printf( "open file error\n" );
		return(-1);
	}
	fprintf( file, "%d\n", pid );
	fclose( file );
	return(0);
}


int program_is_running( char *pidfile )
{
	FILE	*file = fopen( pidfile, "r" );
	pid_t	pid;


	if ( !file )
	{
		printf( "open file error\n" );
		return(0);
	}

	fscanf( file, "%d", &pid );
	fclose( file );

	if ( kill( pid, 0 ) )
	{
		printf( "open file error\n" );
		unlink( pidfile );
		return(0);
	}

	return(1);
}




//--------------------------------







void appendRecord(char *record,char *path){

	FILE* fp = fopen(path, "a+");
	if(fp!=NULL&&record!=NULL){
	  fputs(record,fp);
	  fclose(fp);
	}
}
void ByteToHexStr(const unsigned char* source, char* dest, int sourceLen)

{ 
    short i; 
    unsigned char highByte, lowByte; 


    for (i = 0; i < sourceLen; i++) 
    { 
        highByte = source[i] >> 4; 
        lowByte = source[i] & 0x0f ; 
        highByte += 0x30; 
        if (highByte > 0x39) 
                dest[i * 2] = highByte + 0x07; 
        else 
                dest[i * 2] = highByte; 
        lowByte += 0x30; 
        if (lowByte > 0x39) 
            dest[i * 2 + 1] = lowByte + 0x07; 
        else 
            dest[i * 2 + 1] = lowByte; 
    } 
    return ; 
}
int getInt(u_char * data,int offset){
	int i1=data[offset]&0xff;
	int i2=(data[offset+1]&0xff)<<8;
	int i3=(data[offset+2]&0xff)<<16;
	int i4=(data[offset+3]&0xff)<<24;
	return i1|i2|i3|i4;
}

void myCallback(u_char * uchar, const struct pcap_pkthdr *  packet,const u_char * data){
		int l=54;
		for(;(l+45)<packet->len;l++){
			if(data[l]==46&&data[l+1]==0&&data[l+2]==49&&data[l+3]==11){
                                int nm=l+25;
		                int t1=l+18;
                		int t2=l+19;
				
				char name[14];
                                memset(name, '\0', sizeof(name));
                                strncpy(name,data+nm,12);
                               char record[200];
                                memset(record,'\0',sizeof(record));
                               time_t seconds;
                               seconds = time(NULL);
                               sprintf(record,"%d,%s,%d,%d\n",seconds,name,data[t1],data[t2]);
                               printf("save:%s\n",record);
                               appendRecord(record,savePath);
                
			}else if(data[l]==0x09&&data[l+1]==0x00&&data[l+2]==0x65&&data[l+3]==0x0d&&(l+84)<=packet->len){
				int start=40;
				int nm=l+27;
				int ls=getInt(data,l+start);
				int st=getInt(data,l+start+4);
				int mt=getInt(data,l+start+8);
				int tk=getInt(data,l+start+12);
				int jb=getInt(data,l+start+16);
				char name[14];
				memset(name, '\0', sizeof(name));
				strncpy(name,data+nm,12);
                               char record[200];
                                memset(record,'\0',sizeof(record));
				time_t seconds;
                               seconds = time(NULL);
                               sprintf(record,"%d,%s,%d,%d,%d,%d,%d\n",seconds,name,ls,st,mt,tk,jb);
			       appendRecord(record,storeSavePath);


			}
		}
		//printf("raw data:");
//                for(int i=0;i<packet->len;i++){
 //               printf("%02x",data[i]);
//		}
//		printf("\n");


}
int main(int argc,char *argv[]){
	if ( program_is_running( PROGRAM_PID_FILE ) )
	{
		printf( "the program has benn running.\n" );
		return(0);
	}


/*进程后台运行*/
	demonize();


	if ( program_pid_write( PROGRAM_PID_FILE, getpid() ) )
	{
		printf( "the program pid write err.\n" );
		return(0);
	}


struct sigaction sa;
sa.sa_handler = SIG_IGN;
sa.sa_flags = 0;
sigaction(SIGPIPE,&sa,NULL);
if(argc==1){
  printf("you nedd input ip address");
}

savePath=argv[2];
storeSavePath=argv[3];
//appendRecord("hahahahahahaha");
//appendRecord(savePath);
//appendRecord(argv[1]);
printf("arg1=%s\targ2=%s\nsavePath=%s\n",argv[1],argv[2],savePath);
while(1){
char errbuf[PCAP_ERRBUF_SIZE];//存放错误信息的缓冲
char *result= pcap_lookupdev(errbuf);  
char errbuf2[100];
pcap_t* pPcap_t=pcap_open_live(result,BUFSIZ,0,-1,errbuf2);

//filter config
bpf_u_int32 net;
struct bpf_program filter;

char filter_app[50] = "port 5991 and dst ";
strcat(filter_app,argv[1]);

printf("filter:%s\n",filter_app);
pcap_compile(pPcap_t, &filter, filter_app, 0, net);
pcap_setfilter(pPcap_t,&filter);

unsigned char error_buffer1[100];
	while(1){
		pcap_loop(pPcap_t,-1,myCallback,error_buffer1);
		appendRecord("has a error,but wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwasdsalkjdlasjclkjklsvj;fsjvlksjldjflsdjflkdskjfldsjfldskjfldsj\n",savePath);
	}
}
return 0;
}



