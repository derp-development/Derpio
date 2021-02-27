package me.conclure.derpio.storage;

import me.conclure.derpio.model.user.UserData;

public interface Storage {

  UserData loadUser(Long userId);

  void saveUser(UserData userData);
}
