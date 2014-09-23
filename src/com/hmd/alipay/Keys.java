/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.hmd.alipay;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	//合作身份者id，以2088开头的16位纯数字
	public static final String DEFAULT_PARTNER = "2088611190466844";

	//收款支付宝账号
	public static final String DEFAULT_SELLER = "2088611190466844";

	//商户私钥，自助生成
	public static final String PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANBfnJyk5m/iZhrDJFry5nlfjy1GloT28JedaRYrsRpB6FxLCgGYXZex+JJ+y3E9HSWjmTwuRDO5kg914vCwbvgLdPryI4r8QQDLFhjLPO/jSP9D2NmWmHzxutV3C1KetIKmvKa+ZIhdDSkltrZweaK8vbj6HR4joz3tAGlmSrrlAgMBAAECgYEAh3B+NXshpTu9LkwFB3C2ydGJGN6223o+6+2O7rA7j1ujksnbmSnWO9GcRmxQBCy7E0oBQhK4lf5ap93yQowLs9ZCNSDH6uQ6qWNXd6Q1nz6eL8J3doJAHBCKYfzgvTTmAYpBTkeZfXsXUlx9FjDV5+/XuQ1dx1keCxDcU8Dr1skCQQDrOEupE8gnQa8lrtpKPvvVGAKU6H61mcKdQrMVi5JQLuAdC1OMCzrHm93lxHsxtA1+IqrJyxNBRgt3UogTGgOPAkEA4sgpLfzbSl5xO6DANOC/5u11yU1OhlRT5a8uAgyOq0SbjZT0xGvavEWw6RkGmdd/yqIRwH/hR6eblmMzRwFQSwJAOcyTivB5dmwFhdT5ftULPMmxmLWorScjU1FYgCrTwTBDkd9+oCMCRppeZF9rhkMgLaaHIR7tlFSJ7MAy/76csQJAAIamUcz8VTfS22iMCZNb3pU+aLEdNYKMj0PQv4B39D28fp+R0cLORxGCT4gXU9QEH9wwXlntk9T3r2mbfNar8QJBANS0PnV2T7+WFSfiWzypXwcOWqXGZ8IMBvOwSWUlBV01wdy2dw9yQYDsGDXklbbQN1vynBQensqzqeNd/K7yuH8=";

	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

}
