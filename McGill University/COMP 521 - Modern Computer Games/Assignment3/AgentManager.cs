using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AgentManager : MonoBehaviour
{
    public GameObject agentPrefab;
    public int numberOfAgents;

    public static bool spawningPhase = true;

    private float minX = -7.8f;
    private float maxX = 7.8f;
    private float minY = -4.2f;
    private float maxY = 4.2f;

    private List<GameObject> agents;

    private System.Random rand;
    private bool useSeed = false;
    private int seed = 123456789;

    // Start is called before the first frame update
    void Start()
    {
        agents = new List<GameObject>();
        rand = useSeed ? new System.Random(seed) : new System.Random();
    }

    // Update is called once per frame
    void Update()
    {
        spawningPhase = transform.childCount < numberOfAgents;
        while(transform.childCount < numberOfAgents)
        {
            GameObject agent = GameObject.Instantiate(agentPrefab, transform);
            agents.Add(agent);
            Vector3 pos = agents[agents.Count - 1].transform.localPosition;
            pos.x = (float)(rand.NextDouble()) * (maxX - minX) + minX;
            pos.y = (float)(rand.NextDouble()) * (maxY - minY) + minY;
            agents[agents.Count - 1].transform.localPosition = new Vector3(pos.x, pos.y, pos.z);
            float r = (float)rand.NextDouble();
            float g = (float)rand.NextDouble();
            float b = (float)rand.NextDouble();
            agent.GetComponent<MeshRenderer>().material.SetColor("_Color", new Color(r, g, b));
        }
    }
}
