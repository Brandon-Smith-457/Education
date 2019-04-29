using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class GameBoardChat : MonoBehaviour 
{

    public string username;

    public int maxMessages = 25;

    public GameObject chatPanel, textObject;
    private GameObject scrollView, textBox, text;
    public InputField chatBox;

    public Color playerMessage, info;

    private bool inTextBox;

    [SerializeField]
    List<Message> messageList = new List<Message>();

    private void Start()
    {
        scrollView = GameObject.Find("Scroll View");
        textBox = GameObject.Find("TextBarInputField");
        text = GameObject.Find("ChatText");
        scrollView.SetActive(false);
        textBox.SetActive(false);
        text.SetActive(false);
    }

    private void Update()
    {
        if (Input.GetKeyDown(KeyCode.Return))
        {
            if (!scrollView.activeSelf && !textBox.activeSelf && !text.activeSelf)
            {
                scrollView.SetActive(true);
                textBox.SetActive(true);
                text.SetActive(true);
                chatBox.ActivateInputField();
            }
            else if (chatBox.text == "")
            {
                scrollView.SetActive(false);
                textBox.SetActive(false);
                text.SetActive(false);
                chatBox.DeactivateInputField();
            }
            if (chatBox.text != "")
            {
                SendMessageToChat(username + ": " + chatBox.text, Message.MessageType.playerMessage);
                chatBox.text = "";
                chatBox.ActivateInputField();
            }
        }
    }

    public void SendMessageToChat(string text, Message.MessageType messageType)
    {
        if (messageList.Count >= maxMessages)
        {
            Destroy(messageList[0].textObject.gameObject);
            messageList.Remove(messageList[0]);
        }
        Message newMessage = new Message();
        newMessage.text = text;
        GameObject newText = Instantiate(textObject, chatPanel.transform);
        newMessage.textObject = newText.GetComponent<Text>();
        newMessage.textObject.text = newMessage.text;
        newMessage.textObject.color = MessageTypeColor(messageType);
        messageList.Add(newMessage);
    }

    Color MessageTypeColor(Message.MessageType messageType )
    {
        Color color = info;
        switch(messageType)
        {
            case Message.MessageType.playerMessage:
                color = playerMessage;
                break;
        }

        return color;
    }

}

/*[System.Serializable]
public class Message
{
    public string text;
    public Text textObject;
    public MessageType messageType;

    public enum MessageType
    {
        playerMessage,
        info
    }
}
*/