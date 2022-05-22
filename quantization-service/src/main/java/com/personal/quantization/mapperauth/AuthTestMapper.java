package com.personal.quantization.mapperauth;

import java.util.List;

import com.personal.quantization.model.AuthTest;

public interface AuthTestMapper {

	void saveAuthTest(AuthTest test);

	List<AuthTest> selectAuthTests();

}
