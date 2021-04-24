package com.akokko.service.impl;

import com.akokko.constant.RedisConstant;
import com.akokko.dao.SetmealDao;
import com.akokko.entity.PageResult;
import com.akokko.entity.QueryPageBean;
import com.akokko.pojo.Setmeal;
import com.akokko.service.SetmealService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("out_put_path")
    private String outPutPath;

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //将套餐信息写入到数据库
        setmealDao.add(setmeal);
        //获取存入setmeal的id
        Integer setmealId = setmeal.getId();
        //调用本类方法将套餐与检查组进行连接
        this.connectionSetmealAndCheckgroup(setmealId, checkgroupIds);
        //将文件名称写入到redis
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
        //生成静态页面
        generateMobileStaticHtml();
    }

    /*生成手机静态页面*/
    public void generateMobileStaticHtml() {
        //查询Dao
        List<Setmeal> setmeal = setmealDao.getSetmeal();
        //生成套餐列表页
        generateMobileSetmealHtml(setmeal);
        //生成套餐详情页
        generateMobileSetmealDetailHtml(setmeal);
    }

    /*生成套餐列表页*/
    public void generateMobileSetmealHtml(List<Setmeal> setmeals) {
        Map map = new HashMap();
        map.put("setmealList", setmeals);
        this.generateHtml("mobile_setmeal.ftl","m_setmeal.html", map);
    }

    /*生成套餐详情页面*/
    public void generateMobileSetmealDetailHtml(List<Setmeal> setmeals) {
        for (Setmeal setmeal : setmeals) {
            Map map = new HashMap();
            map.put("setmeal", this.findById(setmeal.getId()));
            this.generateHtml("mobile_setmeal_detail.ftl", "setmeal_detail_" + setmeal.getId() + ".html", map);
        }
    }

    public void generateHtml(String templateName,String htmlPageName,Map map) {
        //获取配置对象
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        //
        Writer writer = null;
        try {
            //生成文件路径
            writer = new FileWriter(new File(outPutPath + "/" + htmlPageName));
            //加载模板文件
            Template template = configuration.getTemplate(templateName);
            //生成文件
            template.process(map, writer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流
            if (null != writer) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //获取参数
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //开启分页助手
        PageHelper.startPage(currentPage, pageSize);
        //调用Dao
        Page<Setmeal> page = setmealDao.findPage(queryString);
        //返回值
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    @Override
    public List<Integer> findGroupBySetmeal(Integer id) {
        //直接返回List集合
        return setmealDao.findGroupBySetmeal(id);
    }

    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        //将新创建的套餐写入数据库
        setmealDao.edit(setmeal);
        //清空之前setmeal的选择项
        setmealDao.deleteCheckgroupIds(setmeal.getId());
        //将现在的checkgroupIds添加绑定
        this.connectionSetmealAndCheckgroup(setmeal.getId(), checkgroupIds);
    }

    @Override
    public void delete(Integer id) {
        //删除绑定的检查组
        setmealDao.deleteCheckgroupIds(id);
        //删除套餐
        setmealDao.delete(id);
    }

    @Override
    public List<Setmeal> getSetmeal() {
        return setmealDao.getSetmeal();
    }

    @Override
    public Setmeal findFullSetmealById(Integer id) {
        return setmealDao.findFullSetmealById(id);
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }

    /**
     * 将套餐与检查组进行绑定
     * @param setmealId
     * @param checkgroupIds
     */
    public void connectionSetmealAndCheckgroup(Integer setmealId, Integer[] checkgroupIds) {
        //判断检查组勾选是否为空，为空则不写入数据库
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            //将套餐与检查组关联
            for (Integer checkgroupId : checkgroupIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("setmealId", setmealId);
                map.put("checkgroupId", checkgroupId);
                setmealDao.connectionSetmealAndCheckgroup(map);
            }
        } else {
            throw new RuntimeException();
        }
    }
}
