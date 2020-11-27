// package com.chaindigg.monitor.common.shiro;
//
// import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
// import com.chaindigg.monitor.common.dao.UserMapper;
// import org.apache.shiro.authc.AuthenticationException;
// import org.apache.shiro.authc.AuthenticationInfo;
// import org.apache.shiro.authc.AuthenticationToken;
// import org.apache.shiro.authc.SimpleAuthenticationInfo;
// import org.apache.shiro.realm.SimpleAccountRealm;
//
// public class MyShiroRealm extends SimpleAccountRealm {
//
//  private UserMapper userMapper;
//
//  @Override
//  public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
//      throws AuthenticationException {
//    String username = token.getPrincipal().toString();
//
//    QueryWrapper queryWrapper = new QueryWrapper();
//    queryWrapper.eq("name", username);
//
//    // TODO
//    String password = userMapper.selectOne(queryWrapper).getEmail();
//
//    if (password != null) {
//      AuthenticationInfo authenticationInfo =
//          new SimpleAuthenticationInfo(
//              username, //                    这里的密码是数据库中的密码
//              password, getName()); //                    返回Realm名
//      return authenticationInfo;
//    }
//    return null;
//  }
// }
