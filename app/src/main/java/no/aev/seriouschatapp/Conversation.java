package no.aev.seriouschatapp;

/**
 * Created by aev on 11.10.17.
 */

public class Conversation
{
    private Long id;
    private String user;
    private String text;

    public Conversation(Long id, String user, String text)
    {
        this.id = id;
        this.user = user;
        this.text = text;
    }

    public Long getId()
    {
        return id;
    }

    public String getUser()
    {
        return user;
    }

    public String getText()
    {
        return text;
    }
}
