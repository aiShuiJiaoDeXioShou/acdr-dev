package cn.iocoder.yudao.module.serialization.handler;

import cn.hutool.json.JSONUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 对象字段转存为Json类型
 * @param <T>
 */
@MappedTypes({List.class})
public class JsonTypeHandler<T> extends BaseTypeHandler<T> {

    private final Class<T> type;

    public JsonTypeHandler(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        // 将对象转换为JSON字符串并设置到PreparedStatement中
        ps.setString(i, JSONUtil.toJsonStr(parameter));
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // 从ResultSet中获取JSON字符串并转换为指定类型的对象
        String jsonString = rs.getString(columnName);
        return JSONUtil.parse(jsonString).toBean(type);
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        // 从ResultSet中获取JSON字符串并转换为指定类型的对象
        String jsonString = rs.getString(columnIndex);
        return JSONUtil.parse(jsonString).toBean(type);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        // 从CallableStatement中获取JSON字符串并转换为指定类型的对象
        String jsonString = cs.getString(columnIndex);
        return JSONUtil.parse(jsonString).toBean(type);
    }
}
