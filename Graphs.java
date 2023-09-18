package DijkstrasAlgo;


import java.io.*;

class makeGraph 
{
    private int[][] adjacencyMatrix;
    private int numVertices;
    private int INFINITY = Integer.MAX_VALUE;
    private int counter = 0;


    public makeGraph(int numVertices) 
    {
        this.numVertices = numVertices;
        adjacencyMatrix = new int[numVertices][numVertices];
    }

    //Make the matrix where (1,5) = 10 means vertex 1 connects to 5 with edge 10
    public void addEdge(int src, int dest, int weight)
    {
        adjacencyMatrix[src - 1][dest - 1] = weight;
        adjacencyMatrix[dest - 1][src - 1] = weight;
    }

    //Allows us to find the lowest weight of none visited verticies that are in our distances table
    public int findMin(int[] distances, boolean[] visited) 
    {
        int minWeight = INFINITY;
        int minWeightVertex = -1;

        for (int i = 0; i < numVertices; i++) 
        {
            if (!visited[i] && distances[i] < minWeight) 
            {
                minWeight = distances[i];
                minWeightVertex = i;
            }
        }

        return minWeightVertex + 1;
    }

   public void floydWarshall() {
        int[][] distances = new int[numVertices][numVertices];


        for (int i = 0; i < numVertices; i++) 
        {
            for (int j = 0; j < numVertices; j++) 
            {
                distances[i][j] = adjacencyMatrix[i][j];
                if (i != j && distances[i][j] == 0) 
                {
                    distances[i][j] = INFINITY;
                }
            }
        }

   
        for (int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    if (distances[i][k] != INFINITY && distances[k][j] != INFINITY && distances[i][k] + distances[k][j] < distances[i][j]) 
                    {
                        distances[i][j] = distances[i][k] + distances[k][j];
                    }
                }
            }
        }

  
        printFloydWarshall(distances, numVertices);
    }

public void printFloydWarshall(int[][] distances, int numVertices) {
    try 
    {
        FileWriter writer = new FileWriter("cop3503-asn2-output-acosta-antonio-fw.txt");
        writer.write(numVertices + "\n");

        for (int i = 0; i < numVertices; i++) 
        {
            for (int j = 0; j < numVertices; j++) 
            {
                if (j > 0) 
                {
                    writer.write(" ");
                }
                if (distances[i][j] == INFINITY) 
                {
                    writer.write("-1");
                } 
                else 
                {
                    writer.write(String.valueOf(distances[i][j]));
                }
            }
            writer.write("\n");
        }
        writer.close();
    } 
    catch (IOException e) 
    {
        System.out.println(e);
    }
}


public void bellmanFord(int sourceNode) 
{
    
    int[] distances = new int[numVertices];
    int[] prevVertex = new int[numVertices];


    for (int i = 0; i < numVertices; i++) 
    {
        distances[i] = INFINITY;
        prevVertex[i] = -1;
    }

    distances[sourceNode - 1] = 0;

    //Loop through all vertices v - 1 times
    for (int i = 0; i < numVertices - 1; i++) 
    {
        //Repeat the process below for all edges
        for (int j = 0; j < numVertices; j++) 
        {
            for (int k = 0; k < numVertices; k++) 
            {
                int weight = adjacencyMatrix[j][k];
                if (weight != 0) 
                {
                    if (distances[j] != INFINITY && distances[j] + weight < distances[k]) {
                        distances[k] = distances[j] + weight;
                        prevVertex[k] = j + 1;
                    }
                }
            }
        }
    }

    printBell(distances, prevVertex, sourceNode, numVertices);
}




   

public void printBell(int[] distances, int[] prevVertex, int sourceNode, int numVertices) 
{
    try 
    {
        FileWriter writer = new FileWriter("cop3503-asn2-output-acosta-antonio-bf.txt");
        writer.write(numVertices + "\n");

        for (int i = 0; i < numVertices; i++) 
        {
            if (i + 1 == sourceNode) 
            {
                writer.write((i + 1) + " " + "0" + " " + "0" + "\n");
            } 
            else 
            {
                writer.write((i + 1) + " " + distances[i] + " " + prevVertex[i] + "\n");
            }
        }
        writer.close();
    } 
    catch (IOException e) 
    {
        System.out.println(e);
    }
}


    public static void main(String[] args) 
    {
        try 
        {
            BufferedReader reader = new BufferedReader(new FileReader("cop3503-asn2-input.txt"));

            int numVertices = Integer.parseInt(reader.readLine()); // Read the number of vertices
            int source = Integer.parseInt(reader.readLine()); // Source Vertex
            int numEdges = Integer.parseInt(reader.readLine()); // Read the number of edges

            makeGraph graph = new makeGraph(numVertices);

            for (int i = 0; i < numEdges; i++) 
            {
                String[] edgeInfo = reader.readLine().split(" ");

                int source1 = Integer.parseInt(edgeInfo[0]);
                int destination = Integer.parseInt(edgeInfo[1]);
                int weight = Integer.parseInt(edgeInfo[2]);

                graph.addEdge(source1, destination, weight);
            }

            reader.close();


            graph.bellmanFord(source);
            graph.floydWarshall();

        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
