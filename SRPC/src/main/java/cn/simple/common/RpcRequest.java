/**
 * 版权所有@: 杭州铭师堂教育科技发展有限公司
 * 注意：本内容仅限于杭州铭师堂教育科技发展有限公司内部使用，禁止外泄以及用于其他的商业目的
 * CopyRight@: 2018 Hangzhou Mistong Educational Technology Co.,Ltd.
 * All Rights Reserved.
 * Note:Just limited to use by Mistong Educational Technology Co.,Ltd. Others are forbidden.
 */
package cn.simple.common;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

/**
 * RpcRequest
 *
 * Created by huapeng.hhp on 2018/5/1.
 */
public class RpcRequest implements Serializable {

	private static final AtomicLong REQUEST_ID = new AtomicLong(1L);
	private Long requestId;
	private String className;
	private String methodName;
	private Class<?>[] paramTypes;
	private Object[] params;

	public RpcRequest() {
		this.requestId = REQUEST_ID.getAndIncrement();
	}

	public RpcRequest(String className, String methodName, Class<?>[] paramTypes, Object[] params) {
		this.requestId = REQUEST_ID.getAndIncrement();
		this.className = className;
		this.methodName = methodName;
		this.paramTypes = paramTypes;
		this.params = params;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Class<?>[] getParamTypes() {
		return paramTypes;
	}

	public void setParamTypes(Class<?>[] paramTypes) {
		this.paramTypes = paramTypes;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return "RpcRequest{" + "requestId='" + requestId + '\'' + ", className='" + className + '\'' + ", methodName='"
				+ methodName + '\'' + ", paramTypes=" + Arrays.toString(paramTypes) + ", params="
				+ Arrays.toString(params) + '}';
	}
}
