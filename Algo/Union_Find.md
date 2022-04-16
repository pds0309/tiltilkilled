


## 유니온 파인드에 대해 알아보자



### Disjoint Set

- 서로 중복되지 않는 부분 집합들로 나눠진 원소들에 대한 정보를 저장하고 조작하는 자료구조


### Union Find?

- `Disjoint Set` 을 표현할 때 사용하는 알고리즘이다.

- 다음 연산을 이용해 Disjoint Set 을 표현

**Union(X,Y)**

- `X`가 속한 집합과 `Y`가 속한 집합을 합침

**Find(X)**

- `X`가 속한 집합의 대표값을 반환 (근본)

<br>


### Union-Find 적용 예시

- 전체 집합이 있는데 구성 원소들이 겹치지 않도록 분할하는데 사용된다.

Kruskal MST 알고리즘에서 새로 추가할 간선의 추가 시 사이클 형성 여부 검사할 때

<br>

### 구현 - 기본


```java

int root[SIZE];

// find
int find(int x) {

    if(root[x] == x) {
        return x;
    }
    return find(root[x]);
}

// union
void union(int x, int y) {
    x = find(x);
    y = find(y);
    root[y] = x;    
}


```


### 구현 - find 연산 최적화

- 트리의 높이가 낮을 수록 근본노드 탐색이 빨라진다.

- 미리 최대한 루트노드와 직접적으로 연결시켜 주는 것이다.

- 기본 find의 경우 최악의 시간 복잡도 `O(n)`)

- 최적화 find 의 경우  시간 복잡도 O(logN)

```java

int find(int x) {
  if (root[x] == x) {
      return x;
  } 
  return root[x] = find(root[x]);
}

```

### 구현- union 연산 최적화

- 트리의 높이가 낮을 수록 연산 수행이 좋다.

- 노드의 수가 더 많은 트리에 작은 트리를 합칠 수록 전체 노드의 Level 이 작아짐



### java


```java
class UnionFind {
    int[] parent;
    int[] rank;

    public UnionFind(int size) {
        parent = new int[size];
        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;
        }
        Arrays.fill(rank, 1);
    }

    public int find(int x) {
        if(x != parent[x]) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public void union(int x, int y) {
        x = find(x);
        y = find(y);
        if(x != y) {
            if(rank[x] > rank[y]) {
                parent[y] = x;
            } else if (rank[x] < rank[y]) {
                parent[x] = y;
            } else {
                parent[y] = x;
                rank[x] += 1;
            }
        }
    }

    public boolean isConnected(int x, int y) {
        return find(x) == find(y);
    }
}
```

