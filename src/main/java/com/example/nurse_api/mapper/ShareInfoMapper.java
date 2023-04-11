package com.example.nurse_api.mapper;

import com.example.nurse_api.pojo.ShareDto;
import com.example.nurse_api.pojo.ShareInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShareInfoMapper {
    List<ShareDto> selectAll();
    int insert(ShareInfo shareInfo);
}
