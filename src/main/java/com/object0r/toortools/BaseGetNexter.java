package com.object0r.toortools;
/**
 * Created by User on 11/4/2015.
 */
public abstract class BaseGetNexter
{
    //must be synchronized
    abstract Object getNext();

    //save session somehow
    abstract void save();

    abstract void loadLatestDone();

}
