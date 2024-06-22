package xyz.ontip.mapper.token;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TokenMapper {

    @Select("select token from token_block")
    List<String> getTokenList();

    int batchInsertToken(@Param("tokens") List<String> tokens);

    int batchDeleteToken(@Param("tokens") List<String> tokens);
}
