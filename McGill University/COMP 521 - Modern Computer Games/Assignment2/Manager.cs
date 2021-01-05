using System;
using UnityEngine;
using UnityEngine.UI;

public class Manager : MonoBehaviour
{
    public GameObject line;                             // In scene line. 
    public GameObject point;                            // In scene point. 
    public Text text;                                   // In scene text. 

    private GameObject m_instantiated_point;            // Point we will instantiate. 


    private Color m_point_color;                        // Color and position variables. 
    private Vector3 m_point_position;
    private Color m_instantiated_point_color;
    private Vector3 m_instantiated_point_position;

    private uint counter = 0;                           // Counter variable to demonstrate text. 

    // Start is called before the first frame update
    void Start()
    {
        m_instantiated_point_color = new Color(0.7f, 0.5f, 0.2f);                                               // Setting color variable. 

        m_instantiated_point = Instantiate(Resources.Load("Point", typeof(GameObject))) as GameObject;          // How to instantiate a GameObject. Change "Point" to "Line" to instantiate a line. 
        SpriteRenderer isr = m_instantiated_point.GetComponent<SpriteRenderer>();                               // Getting the point sprite renderer. Used to access point color. 
        isr.color = m_instantiated_point_color;                                                                 // Setting the point color. 
    }

    // Update is called once per frame
    void Update()
    {
        m_instantiated_point_position = new Vector3(4 * (float)Math.Cos(1.7f * Time.time), 2 * (float)Math.Sin(Time.time), 0);          // Setting variables. 
        m_point_position = new Vector3(3*(float)Math.Cos(Time.time), 5*(float)Math.Sin(1.9f * Time.time), 0);
        m_point_color = new Color((float)Math.Cos(Time.time), 0.5f, 0.5f);

        point.transform.position = m_point_position;                                // Setting point position. 
        m_instantiated_point.transform.position = m_instantiated_point_position;

        SpriteRenderer sr = point.GetComponent<SpriteRenderer>();                   // Getting the point sprite renderer. Used to access point color. 
        LineRenderer lineRenderer = line.GetComponent<LineRenderer>();              // Getting the line renderer. Used to access line color and line end positions. 

        sr.color = m_point_color;                                                   // Setting the point color. 
        lineRenderer.SetPosition(0, m_point_position);                              // Setting the line start position. 
        lineRenderer.SetPosition(1, m_instantiated_point_position);                 // Setting the line end position. 
        lineRenderer.startColor = m_point_color;                                    // Setting the line start color. 
        lineRenderer.endColor = m_instantiated_point_color;                         // Setting the line end color. 

        text.text = "Hello World " + counter;                                       // Setting text content. 

        if (Input.GetKey("a"))                                                      // Getting a key press from the user. 
        {
            counter++;
            counter %= 10;
        }
    }
}
