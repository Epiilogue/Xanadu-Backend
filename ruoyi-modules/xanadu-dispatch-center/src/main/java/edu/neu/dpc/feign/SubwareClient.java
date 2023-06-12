package edu.neu.dpc.feign;


import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "xanadu-ware")
public interface SubwareClient {


}
