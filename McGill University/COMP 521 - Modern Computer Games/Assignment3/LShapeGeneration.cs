using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LShapeGeneration : MonoBehaviour
{
    public GameObject lPiece;
    public GameObject node;
    public Transform visibilityGraph;

    private bool useSeed = false;
    private int seed = 123456789;

    private float minLengthScale = 0.5f;
    private float maxLengthScale = 2.0f;
    private float minWidthScale = 0.25f;
    private float maxWidthScale = 1.0f;

    private float nodeOffset = 0.2f;
    private float nodeHeight = -0.157f;

    void Awake()
    {
        System.Random rand = useSeed ? new System.Random(seed) : new System.Random();
        int numLShapes = rand.Next(3, 5);
        for (int i = 0; i < numLShapes; i++)
        {
            // xSign is for controlling whether we're a leftVerticalLPiece or a rightVerticalLPiece
            int xSign = (rand.Next(0, 2) == 1) ? 1 : -1;
            // ySign is for controlling whether we're extending downwards or upwards
            int ySign = (rand.Next(0, 2) == 1) ? 1 : -1;

            float horizontalXScale = (float)(rand.NextDouble()) * (maxLengthScale - minLengthScale) + minLengthScale;
            float horizontalYScale = (float)(rand.NextDouble()) * (maxWidthScale - minWidthScale) + minWidthScale;
            float verticalXScale = (float)(rand.NextDouble()) * (maxWidthScale - minWidthScale) + minWidthScale;
            float verticalYScale = (float)(rand.NextDouble()) * (maxLengthScale - minLengthScale) + minLengthScale;

            while (verticalYScale <= horizontalYScale)
            {
                horizontalYScale = (float)(rand.NextDouble()) * (maxWidthScale - minWidthScale) + minWidthScale;
                verticalYScale = (float)(rand.NextDouble()) * (maxLengthScale - minLengthScale) + minLengthScale;
            }

            GameObject piece = GameObject.Instantiate(lPiece, transform);
            Transform horizontalPiece = piece.transform.GetChild(0);
            Transform verticalPiece = piece.transform.GetChild(1);

            Vector3 pos = new Vector3(-5.0f + i*(10.0f / (float)(numLShapes - 1)), (float)(rand.NextDouble()) * 4.0f - 2.0f, 0.0f);
            piece.transform.position = pos;

            horizontalPiece.localScale = new Vector3(horizontalXScale, horizontalYScale, 1.0f);
            pos = verticalPiece.localPosition;
            pos.x += xSign + (horizontalXScale - 1.0f) * xSign / 2.0f;
            pos.y -= (horizontalYScale - 1.0f) * ySign / 2.0f;
            verticalPiece.localPosition = pos;

            verticalPiece.localScale = new Vector3(verticalXScale, verticalYScale, 1.0f);
            pos.x += (verticalXScale - 1.0f) * xSign / 2.0f;
            pos.y += (verticalYScale - 1.0f) * ySign / 2.0f;
            verticalPiece.localPosition = pos;

            BoxCollider horizBoxCol = horizontalPiece.gameObject.GetComponent<BoxCollider>();
            BoxCollider vertiBoxCol = verticalPiece.gameObject.GetComponent<BoxCollider>();

            //Vector3 horizMin = horizBoxCol.center - horizBoxCol.size * 0.5f;
            Vector3 horizMin = horizontalPiece.position - horizontalPiece.localScale * 0.5f;
            //Vector3 horizMax = horizBoxCol.center + horizBoxCol.size * 0.5f;
            Vector3 horizMax = horizontalPiece.position + horizontalPiece.localScale * 0.5f;
            Vector3 horizBL = new Vector3(horizMin.x, horizMin.y, nodeHeight);
            Vector3 horizBR = new Vector3(horizMax.x, horizMin.y, nodeHeight);
            Vector3 horizTR = new Vector3(horizMax.x, horizMax.y, nodeHeight);
            Vector3 horizTL = new Vector3(horizMin.x, horizMax.y, nodeHeight);

            //Vector3 vertiMin = vertiBoxCol.center - vertiBoxCol.size * 0.5f;
            Vector3 vertiMin = verticalPiece.position - verticalPiece.localScale * 0.5f;
            //Vector3 vertiMax = vertiBoxCol.center + vertiBoxCol.size * 0.5f;
            Vector3 vertiMax = verticalPiece.position + verticalPiece.localScale * 0.5f;
            Vector3 vertiBL = new Vector3(vertiMin.x, vertiMin.y, nodeHeight);
            Vector3 vertiBR = new Vector3(vertiMax.x, vertiMin.y, nodeHeight);
            Vector3 vertiTR = new Vector3(vertiMax.x, vertiMax.y, nodeHeight);
            Vector3 vertiTL = new Vector3(vertiMin.x, vertiMax.y, nodeHeight);

            if (xSign > 0 && ySign > 0)
            {
                spawnNode(horizBL, -1, -1);
                // horizontal bottom right && vertical bottom left ignored
                spawnNode(vertiBR, 1, -1);
                spawnNode(vertiTR, 1, 1);
                spawnNode(vertiTL, -1, 1);
                // Horizontal top right node spawn flipped over tangent of rightmost edge
                spawnNode(horizTR, -1, 1);
                spawnNode(horizTL, -1, 1);
            }
            else if (xSign > 0 && ySign < 0)
            {
                spawnNode(horizBL, -1, -1);
                // Horizontal bottom right node spawn flipped over tangent of rightmost edge
                spawnNode(horizBR, -1, -1);
                spawnNode(vertiBL, -1, -1);
                spawnNode(vertiBR, 1, -1);
                spawnNode(vertiTR, 1, 1);
                // Horizontal top right && vertical top left ignored
                spawnNode(horizTL, -1, 1);
            }
            else if (xSign < 0 && ySign > 0)
            {
                // Horizontal bottom left && vertical bottom right ignored
                spawnNode(horizBR, 1, -1);
                spawnNode(horizTR, 1, 1);
                // Horizontal top left node spawn flipped over tangent of leftmost edge
                spawnNode(horizTL, 1, 1);
                spawnNode(vertiTR, 1, 1);
                spawnNode(vertiTL, -1, 1);
                spawnNode(vertiBL, -1, -1);
            }
            else if (xSign < 0 && ySign < 0)
            {
                // Horizontal bottom left node spawn flipped over tangent of leftmost edge
                spawnNode(horizBL, 1, -1);
                spawnNode(horizBR, 1, -1);
                spawnNode(horizTR, 1, 1);
                // Horizontal top left && vertical top right ignored
                spawnNode(vertiTL, -1, 1);
                spawnNode(vertiBL, -1, -1);
                spawnNode(vertiBR, 1, -1);
            }
        }
    }

    private void spawnNode(Vector3 vertex, int xDirection, int yDirection)
    {
        Vector3 pos = new Vector3(vertex.x, vertex.y, vertex.z);
        Transform trans = GameObject.Instantiate(node, visibilityGraph).transform;
        pos.x += nodeOffset * xDirection;
        pos.y += nodeOffset * yDirection;
        trans.position = pos;
    }
}
