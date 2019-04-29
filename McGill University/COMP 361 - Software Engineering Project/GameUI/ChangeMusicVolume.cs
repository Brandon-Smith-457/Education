using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class ChangeMusicVolume : MonoBehaviour
{
    public Slider Volume;
    public AudioSource mymusic;
    // Start is called before the first frame update

    private void Awake()
    {
     

        DontDestroyOnLoad(transform.gameObject);
    }


  
    void Update()
    {
        mymusic.volume = Volume.value;
    }
}
