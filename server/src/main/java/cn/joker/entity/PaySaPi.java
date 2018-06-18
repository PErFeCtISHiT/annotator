package cn.joker.entity;

/**
 * 支付回调的参数实体类
 * 
 * @author zhoutingting
 */
public class PaySaPi {

	/**
	 * paysapi生成的订单ID号
	 */
	private String paysapiId;

	/**
	 * 您的自定义订单号
	 */
	private String orderId;

	/**
	 * 订单定价
	 */
	private String price;

	/**
	 * 实际支付金额
	 */
	private String realPrice;

	/**
	 * 您的自定义用户ID
	 */
	private String orderuid;

	/**
	 * 秘钥
	 */
	private String key;

	public String getPaysapiId() {
		return paysapiId;
	}

	public void setPaysapiId(String paysapiId) {
		this.paysapiId = paysapiId;
	}

	public String getOrderid() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getRealprice() {
		return realPrice;
	}

	public void setRealPrice(String realPrice) {
		this.realPrice = realPrice;
	}

	public String getOrderuid() {
		return orderuid;
	}

	public void setOrderuid(String orderuid) {
		this.orderuid = orderuid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
