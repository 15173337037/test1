package com.itheima.ssm.service.Impl;

import com.itheima.ssm.dao.IuserInfoDao;
import com.itheima.ssm.domain.Role;
import com.itheima.ssm.domain.UserInfo;
import com.itheima.ssm.service.IuserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("userService")
@Transactional
public class UserServiceImpl implements IuserService {

    @Autowired
    private IuserInfoDao iuserInfoDao;
    @Autowired//spring-security提供的加密的对象
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) {
        UserInfo userInfo = null;
        try {
            userInfo = iuserInfoDao.findByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //User user = new User(userInfo.getUsername(),"{noop}"+userInfo.getPassword(),getAuthority(userInfo.getRoles()));
        User user = new User(userInfo.getUsername(),userInfo.getPassword(),userInfo.getStatus()==0?false:true,true,true,true,getAuthority(userInfo.getRoles()));
        return user;


        //缺少一个权限用户的集合,就需要创建一个方法返回的就是这个对象的集合,由于该对象时接口所以返回的是该对象的实现类simple。。
        //iUserInfoDao信息需要补全，里面的List<Role>对象需要根据userid通过user_role中间表进行多对多查询查询出List<Role>
    }

    public List<SimpleGrantedAuthority> getAuthority(List<Role> roles){
        List<SimpleGrantedAuthority> list = new ArrayList<SimpleGrantedAuthority>();
        for (Role role : roles) {
            list.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));
        }
        return list;
    }

    /**
     * 查询所有角色
     * @return
     * @throws Exception
     */
    @Override
    public List<UserInfo> findAll() throws Exception {

        return iuserInfoDao.findAll();
    }

    @Override
    public void save(UserInfo userInfo) throws Exception {
        userInfo.setPassword(bCryptPasswordEncoder.encode(userInfo.getPassword()));
        iuserInfoDao.save(userInfo);
    }

    @Override
    public UserInfo findById(String id)throws Exception {
        return iuserInfoDao.findById(id);

    }

    @Override
    public List<Role> findOtherRoles(String userId) throws Exception{
        return iuserInfoDao.findOtherRoles(userId);
    }

    @Override
    public void addRoleToUser(String userId, String[] roleIds) throws Exception{
        for (String roleId : roleIds) {
            iuserInfoDao.addRoleToUser(userId,roleId);
        }
    }
}
