package org.BatisExampleApp.mapper;

import org.BatisExampleApp.entity.Owner;
import org.BatisExampleApp.entity.Pet;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OwnerMapper {
    @Select("select * from owner where name = #{n}")
    Owner findOneByName(@Param("n") String name);

    @Insert("Insert into Owner (name) values (#{name})")
    void create(Owner owner);


    @Select("select * from owner")
    List<Owner> findAll();


    @Update("UPDATE owner SET name=#{name} WHERE id =#{id}")
    void save(Owner owner);

    @Delete("DELETE FROM owner WHERE id =#{id}")
    void delete(Owner owner);

    @Select("select count(id) from Owner")
    Integer count();

    @Select({"<script>",
            "SELECT *",
            "FROM Owner",
            "WHERE name IN",
            "<foreach item='item' index='index' collection='array'",
            "open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"})
    List<Owner> findByNames(String... names);

    @Delete({"<script>",
            "DELETE",
            "FROM Owner",
            "WHERE id IN",
            "<foreach item='item' index='index' collection='list'",
            "open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"})
    void deleteAllWhereIdIn(List<Long> ids);

    @Delete("DELETE FROM owner")
    void deleteAll();




    @Select("SELECT * FROM Owner")
    @Results(value = {
            @Result(property="id", column = "id"),
            @Result(property="name", column = "name"),
            @Result(property="pets", column="id", javaType= List.class, many=@Many(select="selectPets"))
    })
    List<Owner> findAllWithPets();
    /*public List<Team> getAllTeams();*/

    @Select("SELECT * FROM Pet WHERE owner_id = #{owner_id}")
    @Results(value={
            @Result(property="id", column ="id" ),
            @Result(property="name", column = "name")
    })
    List<Pet> selectPets(String owner_id);
}
