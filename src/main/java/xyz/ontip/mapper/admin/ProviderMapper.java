package xyz.ontip.mapper.admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import xyz.ontip.pojo.entity.Provider;

import java.util.List;

@Mapper
public interface ProviderMapper {
    List<Provider> getAllProvider();
}
