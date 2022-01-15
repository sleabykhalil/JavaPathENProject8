package userApi.dto;

import org.mapstruct.Mapper;
import userApi.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User userDtoToUser(UserDto userDto);
}
