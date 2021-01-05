using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public interface ICollider
{
    bool isColliding(ICollider other);
    int getId();
    void setId(int id);
}
