/**
 * 版权所有@: 杭州铭师堂教育科技发展有限公司
 * 注意：本内容仅限于杭州铭师堂教育科技发展有限公司内部使用，禁止外泄以及用于其他的商业目的
 * CopyRight@: 2018 Hangzhou Mistong Educational Technology Co.,Ltd.
 * All Rights Reserved.
 * Note:Just limited to use by Mistong Educational Technology Co.,Ltd. Others are forbidden.
 */
package cn.simple.common;

import java.io.Serializable;

/**
 * RpcResponse
 *
 * Created by huapeng.hhp on 2018/5/4.
 */
public class RpcResponse implements Serializable {

	private static final long serialVersionUID = 7007519217581044040L;
	private Long requestId;
	private Throwable error;
	private Object result;

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public Throwable getError() {
		return error;
	}

	public void setError(Throwable error) {
		this.error = error;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "RpcResponse{" + "requestId='" + requestId + '\'' + ", error=" + error + ", result=" + result + '}';
	}
}
