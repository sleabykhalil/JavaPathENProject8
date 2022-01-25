package userApi.dto;

import userApi.model.User;

public interface UserMapper {

    User userDtoToUser(UserDto userDto);
}
