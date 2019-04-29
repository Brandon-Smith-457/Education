using UnityEngine;
using UnityEngine.Events;
using UnityEngine.UI;

public class PopupManager
{
    /* Singleton-related stuff */
    private static PopupManager instance = null;
    private static readonly object padlock = new object();
    
    public static PopupManager Instance
    {
        get
        {
            lock (padlock)
            {
                if (instance == null)
                {
                    instance = new PopupManager();
                }
                return instance;
            }
        }
    }

    /* Actual PopupManager behaviour */

    public GameObject gamePanel;

    public GameObject popupPanelPrefab;
    public GameObject popupButtonPrefab;

    private PopupManager()
    {
        popupPanelPrefab = Resources.Load("Prefabs/" + "PopupPanel") as GameObject;
        popupButtonPrefab = Resources.Load("Prefabs/" + "PopupButton") as GameObject;

        //initialize the popup panel and immediately set it as inactive
        gamePanel = UnityEngine.Object.Instantiate(popupPanelPrefab, GameObject.Find("Canvas").transform);
        SetVisible(false);
    }

    public void AddButton(string buttonText, UnityAction buttonAction)
    {
        //this allows you to click on something else on the board and the existing
        //panel will reset itself
        if (gamePanel.activeSelf)
        {
            DeleteButtonsAndHide();
        }
        GameObject newButton = UnityEngine.Object.Instantiate(popupButtonPrefab, gamePanel.transform);
        newButton.transform.GetChild(0).gameObject.GetComponent<Text>().text = buttonText;
        newButton.GetComponent<Button>().onClick.AddListener(buttonAction);
        newButton.GetComponent<Button>().onClick.AddListener(DeleteButtonsAndHide);
    }

    public void SetPositionAndShow(Vector2 position)
    {
        GameObject closeButton = UnityEngine.Object.Instantiate(popupButtonPrefab, gamePanel.transform);
        closeButton.transform.GetChild(0).gameObject.GetComponent<Text>().text = "Close";
        closeButton.GetComponent<Button>().onClick.AddListener(DeleteButtonsAndHide);

        //this was for cells, we'll see if it works for edges
        //hopefully we can get a generic solution

        gamePanel.GetComponent<RectTransform>().anchoredPosition = position;
        SetVisible(true);
    }

    public void DeleteButtonsAndHide()
    {
        SetVisible(false);
        foreach(Transform button in gamePanel.transform)
        {
            UnityEngine.Object.Destroy(button.gameObject);
        }
    }

    private void SetVisible(bool visible)
    {
        gamePanel.SetActive(visible);
    }

}
