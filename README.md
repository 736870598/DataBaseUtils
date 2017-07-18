# DataBaseUtils
DataBaseUtils一款高效及方便使用的数据库框架。可实现自动创建表，一行代码进行增删改查功能。


###使用：

####配置：

1.将SxyDataBaseHelpter_1.0.1.aar导入项目lib 文件夹。

2.在项目Module 下的build.gradle 的根标签中添加以下内容：

    repositories {
        flatDir {
            dirs 'libs'
        }
    }

3.在项目Module 下的build.gradle 的dependencies 标签中添加以下内容：

    compile(name: 'SxyDataBaseHelpter_1.0.1', ext: 'aar')
    
####代码：
 
     DaoManagerFactory factory = DaoManagerFactory.getInstance(path, name);
     //方法一
     UserDao userDao = factory.getDataHelper(UserDao.class, User.class);  
     //方法二 自定义表名
     UserDao userDao = factory.getDataHelper(UserDao.class, User.class, tableName);  
          
 
#####其中:

path 为自定义数据库保存路径

name 为自定义数据库保存名字
    
User 为用户的表结构类（user中的属性只支持基本属性及String, 属性类型请使用封装类）
    
       
        @SxyDBTable("myuser")       
        public class User implements Serializable{       
            @SxyDBField("name")     
            private String name;
            @SxyDBField("password")
            private Integer passWord;
        }
    //注：        
    //@SxyDBTable  默认表名，如果获取dao的时候传入了表名，这改属性失效
    //@SxyDBField  表字段名
    
    //--user中的属性只支持基本属性及String, 属性类型请使用封装类
               
UserDao 必须是BaseDao的子类

    public class UserDao extends BaseDao<User>{...}
    
BaseDao中提供了基本的增、删、改、查、及一些常用的方法，子类可以重新或直接调用其中的方法。

    /**
     * BaseDao
     * Created by sunxiaoyu on 2017/1/13.
     */
    public interface IBaseDao<T> {
    
        /**
         * 获取getSqLiteDatabase
         */
        SQLiteDatabase getSqLiteDatabase();
    
        /**
         * 获取表名
         */
        String getTableName();
        
        /**
         * 增加
         * @return the row ID of the newly inserted row, or -1 if an error occurred
         */
        Long insert(T entity) throws Exception;
    
        /**
         * 修改
         * @return the number of rows affected
         */
        int update(T entity, T where) throws Exception;
    
        /**
         * 删除
         * @return the number of rows affected if a whereClause is passed in, 0
         *         otherwise. To remove all rows and get a count pass "1" as the
         *         whereClause.
         */
        int delete(T where) throws Exception;
    
        /**
         * 查找
         * @return Cursor
         */
        Cursor quert(String sql, String[] strs) throws Exception;
    
        /**
         *  执行
         */
        void execSQL(String string) throws Exception;
    
        /**
         * 是否已经存在
         */
        Boolean exist(T where) throws Exception;
    
        /**
         * 获取表中所用的消息
         */
        List<T> getAllInfo() throws Exception;
    
        /**
         * Cursor 转 model
         */
        T cursor2Model(Cursor curosr) throws Exception;
    
    }
   

    
        
    
            
            
            
            
    
    
    
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     