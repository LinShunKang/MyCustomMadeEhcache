package MyCustomMadeEhcache.model;

import java.util.Date;

/**
 * Created by LinShunkang on 7/9/17.
 */
public class Person {

    private long id;

    private long userId;

    private Date addTime;

    private Date updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", userId=" + userId +
                ", addTime=" + addTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public static Person getInstance(long id) {
        Person person = new Person();
        person.setId(id);
        person.setUserId(1000 + id);
        person.setAddTime(new Date());
        person.setUpdateTime(new Date());
        return person;
    }
}
