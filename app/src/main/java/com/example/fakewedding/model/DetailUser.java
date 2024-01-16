package com.example.fakewedding.model;

public class DetailUser {

       private String   device_register, email, ip_register,
               link_avatar, user_name;

       public DetailUser(String device_register, String email, String ip_register, String link_avatar, String user_name, int count_comment, int count_sukien, int count_view, int id_user) {
              this.device_register = device_register;
              this.email = email;
              this.ip_register = ip_register;
              this.link_avatar = link_avatar;
              this.user_name = user_name;
              this.count_comment = count_comment;
              this.count_sukien = count_sukien;
              this.count_view = count_view;
              this.id_user = id_user;
       }

       private int count_comment, count_sukien, count_view, id_user;


       public String getDevice_register() {
              return device_register;
       }

       public void setDevice_register(String device_register) {
              this.device_register = device_register;
       }

       public String getEmail() {
              return email;
       }

       public void setEmail(String email) {
              this.email = email;
       }

       public String getIp_register() {
              return ip_register;
       }

       public void setIp_register(String ip_register) {
              this.ip_register = ip_register;
       }

       public String getLink_avatar() {
              return link_avatar;
       }

       public void setLink_avatar(String link_avatar) {
              this.link_avatar = link_avatar;
       }

       public String getUser_name() {
              return user_name;
       }

       public void setUser_name(String user_name) {
              this.user_name = user_name;
       }

       public int getCount_comment() {
              return count_comment;
       }

       public void setCount_comment(int count_comment) {
              this.count_comment = count_comment;
       }

       public int getCount_sukien() {
              return count_sukien;
       }

       public void setCount_sukien(int count_sukien) {
              this.count_sukien = count_sukien;
       }

       public int getCount_view() {
              return count_view;
       }

       public void setCount_view(int count_view) {
              this.count_view = count_view;
       }

       public int getId_user() {
              return id_user;
       }

       public void setId_user(int id_user) {
              this.id_user = id_user;
       }
}
