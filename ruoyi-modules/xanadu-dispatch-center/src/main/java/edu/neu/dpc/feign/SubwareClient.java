package edu.neu.dpc.feign;


import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "xanadu-ware", path = "/ware/subware")
public interface SubwareClient {
}
