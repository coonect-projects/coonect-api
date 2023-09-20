package me.coonect.coonect.member.application.port.out.persistence;

public interface VerifiedEmailRepository {

  void save(String code, String email);

  String get(String code);

  void remove(String code);

  boolean has(String code);
}
