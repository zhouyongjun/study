package com.zhou.test.vfs;

import com.zhou.core.vfs.VFSService;

public class VFSMain {

	public static void main(String[] args) {
		try {
			VFSService.getInstance().startup();
			while (true) {
				Thread.sleep(1000);
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
