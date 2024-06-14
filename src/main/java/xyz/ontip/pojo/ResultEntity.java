package xyz.ontip.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ResultEntity<T> implements Serializable {
    private Integer code;
    private T data;
    private String msg;

    // 默认构造函数，成功状态
    public ResultEntity() {
        this.code = 200;
        this.msg = "请求成功";
        this.data = null;
    }

    // 带数据的成功构造函数
    public ResultEntity(T data) {
        this.code = 200;
        this.msg = "请求成功";
        this.data = data;
    }

    // 带状态码和消息的构造函数
    public ResultEntity(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    // 带状态码、消息和数据的构造函数
    public ResultEntity(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 静态方法，用于返回成功的响应
    public static <T> ResultEntity<T> success() {
        return new ResultEntity<>();
    }

    public static <T> ResultEntity<T> success(T data) {
        return new ResultEntity<>(data);
    }

    public static <T> ResultEntity<T> success(String msg, T data) {
        return new ResultEntity<>(200, msg, data);
    }

    // 静态方法，用于返回失败的响应
    public static <T> ResultEntity<T> failure(Integer code, String msg) {
        return new ResultEntity<>(code, msg, null);
    }

    // 静态方法，用于返回服务器内部错误的响应
    public static <T> ResultEntity<T> serverError() {
        return new ResultEntity<>(500, "Server internal error", null);
    }
}

