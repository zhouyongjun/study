
package com.zhou.test.whitelist;

import com.zhou.core.whitelist.WhitelistService;

public class WhitelistMain {
	public static void main(String[] args) {
		try {
			WhitelistService.getInstance().startup();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
