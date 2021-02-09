#include <stdio.h>
#include <stdlib.h>
#include <pcap/pcap.h>
int test()
{
  char errbuf[PCAP_ERRBUF_SIZE];//存放错误信息的缓冲
  pcap_if_t *it;
  int r;
  char source[PCAP_ERRBUF_SIZE+1];
 r=pcap_findalldevs(&it,errbuf); 
 // r=pcap_findalldevs_ex(source,NULL,&it,errbuf);
  if(r==-1)
  {
    printf("err:%s\n",errbuf);
    exit(-1);
  }
  
  while(it)
  {
    printf(":%s\n",it->name);
    
    it=it->next;

  }
}
