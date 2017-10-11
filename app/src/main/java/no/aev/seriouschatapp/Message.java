package no.aev.seriouschatapp;

/**
 * Created by aev on 11.10.17.
 */

public class Message
{

    private Long id;
    private String text;
    private String user;

    public Message(Long id, String text, String user)
    {
        this.id = id;
        this.text = text;
        this.user = user;
    }

    public Long getId()
    {
        return id;
    }

    public String getText()
    {
        return text;
    }

    public String getUser()
    {
        return user;
    }
}
