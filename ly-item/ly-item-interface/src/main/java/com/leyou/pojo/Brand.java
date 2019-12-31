package com.leyou.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Data
@Table(name="tb_brand")
public class Brand {
    @Id
    @KeySql(useGeneratedKeys = true)
    private  Long id;
    private  String name;
    private String image;
    private  Character letter;

}
