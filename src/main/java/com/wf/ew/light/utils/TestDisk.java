package com.wf.ew.light.utils;

import java.io.File;

public class TestDisk {

	public static void main(String[] args) {
		File diskPartition = new File("D:");
		 
        long totalCapacity = diskPartition.getTotalSpace(); 
 
        long freePartitionSpace = diskPartition.getFreeSpace(); 
        long usablePatitionSpace = diskPartition.getUsableSpace(); 
 
        System.out.println("**** Sizes in Mega Bytes ****\n");
 
        System.out.println("Total C partition size : " + totalCapacity / (1024*1024) + " MB");
        System.out.println("Usable Space : " + usablePatitionSpace / (1024 *1024) + " MB");
        System.out.println("Free Space : " + freePartitionSpace / (1024 *1024) + " MB");
 
        System.out.println("\n**** Sizes in Giga Bytes ****\n");
 
        System.out.println("Total C partition size : " + totalCapacity / (1024*1024*1024) + " GB");
        System.out.println("Usable Space : " + usablePatitionSpace / (1024 *1024*1024) + " GB");
        System.out.println("Free Space : " + freePartitionSpace / (1024 *1024*1024) + " GB");
        

	}
	
}
