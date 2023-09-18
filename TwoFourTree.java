package TwoFourTree;

public class TwoFourTree {
    private class TwoFourTreeItem {
        int values = 1;
        int value1 = 0; // always exists.
        int value2 = 0; // exists iff the node is a 3-node or 4-node.
        int value3 = 0; // exists iff the node is a 4-node.
        boolean isLeaf = true;

        TwoFourTreeItem parent = null; // parent exists iff the node is not root.
        TwoFourTreeItem leftChild = null; // left and right child exist iff the note is a non-leaf.
        TwoFourTreeItem rightChild = null;
        TwoFourTreeItem centerChild = null; // center child exists iff the node is a non-leaf 3-node.
        TwoFourTreeItem centerLeftChild = null; // center-left and center-right children exist iff the node is a
                                                // non-leaf 4-node.
        TwoFourTreeItem centerRightChild = null;

        public boolean isTwoNode() {
            if (this.values == 1) {
                return true;
            }
            return false;
        }

        public boolean isThreeNode() {
            if (this.values == 2) {
                return true;
            }
            return false;
        }

        public boolean isFourNode() {
            if (this.values == 3) {
                return true;
            }
            return false;
        }

        public TwoFourTreeItem(int value1) {

            this.value1 = value1;

        }

        public TwoFourTreeItem(int value1, int value2) {

            this.value1 = value1;
            this.value2 = value2;

        }

        public TwoFourTreeItem(int value1, int value2, int value3) {

            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;

        }

        private void printIndents(int indent) {
            for (int i = 0; i < indent; i++)
                System.out.printf("  ");
        }

        public void printInOrder(int indent) {
            if (!isLeaf)
                leftChild.printInOrder(indent + 1);
            printIndents(indent);
            System.out.printf("%d\n", value1);
            if (isThreeNode()) {
                if (!isLeaf)
                    centerChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
            } else if (isFourNode()) {
                if (!isLeaf)
                    centerLeftChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
                if (!isLeaf)
                    centerRightChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value3);
            }
            if (!isLeaf)
                rightChild.printInOrder(indent + 1);
        }
    }

    TwoFourTreeItem root = null;

    public void twoNodeSplit(TwoFourTreeItem root, int value) {

        if (root.parent == null && root.isLeaf == true) {
            // Split case where we hit a 4 node root without children yet
            root.leftChild = new TwoFourTreeItem(root.value1);
            root.rightChild = new TwoFourTreeItem(root.value3);
            root.isLeaf = false;
            root.value1 = root.value2;
            root.values = 1;
            root.leftChild.values = 1;
            root.rightChild.values = 1;
            root.rightChild.parent = root;
            root.leftChild.parent = root;
            addValue(value);

        } else if (root.parent == null && root.isLeaf == false) {

            // Split case where we hit a 4 node root with children
            TwoFourTreeItem newRoot = new TwoFourTreeItem(root.value2);
            TwoFourTreeItem newRight = new TwoFourTreeItem(root.value3);
            TwoFourTreeItem newLeft = new TwoFourTreeItem(root.value1);
            newLeft.isLeaf = false;
            newRight.isLeaf = false;
            newRight.parent = newRoot;
            newLeft.parent = newRoot;
            newLeft.values = 1;
            newRight.values = 1;
            newRoot.leftChild = newLeft;
            newRoot.rightChild = newRight;
            newRoot.rightChild.rightChild = root.rightChild;
            newRoot.rightChild.leftChild = root.centerRightChild;
            newRoot.leftChild.leftChild = root.leftChild;
            newRoot.leftChild.rightChild = root.centerLeftChild;
            newRoot.leftChild.rightChild.parent = newRoot.leftChild;
            root.leftChild.parent = newRoot.leftChild;
            root.centerRightChild.parent = newRoot.rightChild;
            root.rightChild.parent = newRoot.rightChild;

            newRoot.values = 1;

            newRoot.isLeaf = false;

            this.root = newRoot;
            addValue(value);

        } else {

            if (root.parent.isTwoNode()) {

                // Split case where the parent is a 2 node
                if (value > root.parent.value1) {
                    root.parent.value2 = root.value2;
                    root.parent.centerChild = new TwoFourTreeItem(root.value1);
                    root.value1 = root.value3;
                    root.values = 1;
                    root.parent.values = 2;
                    root.parent.centerChild.parent = root.parent;
                    if (root.isLeaf == false) {

                        root.parent.centerChild.leftChild = root.leftChild;
                        root.parent.centerChild.rightChild = root.centerLeftChild;
                        root.parent.centerChild.isLeaf = false;
                        root.parent.rightChild.leftChild = root.centerRightChild;
                        root.parent.rightChild.rightChild = root.rightChild;
                        root.parent.rightChild.isLeaf = false;
                    }
                } else if (value < root.parent.value1) {
                    root.parent.value2 = root.parent.value1;
                    root.parent.value1 = root.value2;
                    root.parent.centerChild = new TwoFourTreeItem(root.value3);
                    root.values = 1;
                    root.parent.values = 2;
                    root.parent.centerChild.parent = root.parent;
                    if (root.isLeaf == false) {
                        root.parent.centerChild.leftChild = root.leftChild;
                        root.parent.centerChild.rightChild = root.centerLeftChild;
                        root.parent.centerChild.isLeaf = false;
                        root.parent.rightChild.leftChild = root.centerRightChild;
                        root.parent.rightChild.rightChild = root.rightChild;
                        root.parent.rightChild.isLeaf = false;
                    }
                }
                addValue(value);
            } else if (root.parent.isThreeNode()) {
                // Split case where the parent is a 3 node
                if (value < root.parent.value1 && value < root.parent.value2) {
                    // this means it's going into the left most node

                    root.parent.value3 = root.parent.value2;
                    root.parent.value2 = root.parent.value1;
                    root.parent.value1 = root.value2;
                    if (value < root.value2) {
                        root.parent.centerLeftChild = new TwoFourTreeItem(root.value3);
                        root.parent.leftChild = root;

                        root.parent.centerRightChild = root.parent.centerChild;
                    } else if (value > root.value2) {
                        root.parent.centerLeftChild = root;
                        root.parent.leftChild = new TwoFourTreeItem(root.value1);
                        root.value1 = root.value3;
                        root.parent.centerRightChild = root.parent.centerChild;
                    }

                    root.parent.centerLeftChild = new TwoFourTreeItem(root.value3);
                    root.values = 1;
                    root.parent.values = 3;

                    root.parent.leftChild.parent = root.parent;
                    if (root.isLeaf == false) {
                        root.parent.centerRightChild.leftChild = root.leftChild;
                        root.leftChild.parent = root.parent.centerRightChild.leftChild;
                        root.parent.centerRightChild.rightChild = root.centerLeftChild;
                        root.centerLeftChild.parent = root.parent.centerRightChild.rightChild;
                        root.parent.centerRightChild.isLeaf = false;
                        root.parent.rightChild.leftChild = root.centerRightChild;
                        root.centerRightChild.parent = root.parent.rightChild.leftChild;
                        root.parent.rightChild.rightChild = root.rightChild;
                        root.rightChild.parent = root.parent.rightChild.rightChild;
                        root.parent.rightChild.isLeaf = false;
                    }

                } else if (value > root.parent.value1 && value < root.parent.value2) {
                    // this means it's going into the middle node
                    root.parent.value3 = root.parent.value2;
                    root.parent.value2 = root.value2;
                    if (value > root.value2) {
                        root.parent.centerLeftChild = new TwoFourTreeItem(root.value1);
                        root.value1 = root.value3;
                        root.parent.centerRightChild = root;
                    } else if (value < root.value2) {
                        root.parent.centerRightChild = new TwoFourTreeItem(root.value3);
                        root.parent.centerLeftChild = root;
                    }
                    root.values = 1;
                    root.parent.values = 3;

                    root.parent.centerRightChild.parent = root.parent;
                    if (root.isLeaf == false) {
                        root.parent.centerRightChild.leftChild = root.leftChild;
                        root.leftChild.parent = root.parent.centerRightChild.leftChild;
                        root.parent.centerRightChild.rightChild = root.centerLeftChild;
                        root.centerLeftChild.parent = root.parent.centerRightChild.rightChild;
                        root.parent.centerRightChild.isLeaf = false;
                        root.parent.rightChild.leftChild = root.centerRightChild;
                        root.centerRightChild.parent = root.parent.rightChild.leftChild;
                        root.parent.rightChild.rightChild = root.rightChild;
                        root.rightChild.parent = root.parent.rightChild.rightChild;
                        root.parent.rightChild.isLeaf = false;
                    }
                } else if (value > root.parent.value1 && value > root.parent.value2) {
                    // this means its going into the right most node
                    root.parent.value3 = root.value2;
                    if (value > root.value2) {
                        root.parent.centerRightChild = new TwoFourTreeItem(root.value1);
                        root.parent.rightChild = root;
                        root.parent.centerLeftChild = root.parent.centerChild;
                        root.value1 = root.value3;

                    } else if (value < root.value2) {
                        root.parent.centerRightChild = root;
                        root.parent.rightChild = new TwoFourTreeItem(root.value3);
                        root.parent.centerRightChild = root.parent.centerChild;

                    }
                    root.parent.values = 3;
                    root.values = 1;
                    root.parent.centerRightChild.parent = root.parent;

                    if (root.isLeaf == false) {

                        root.parent.centerRightChild.leftChild = root.leftChild;

                        root.parent.centerRightChild.rightChild = root.centerLeftChild;

                        root.parent.centerRightChild.isLeaf = false;
                        root.parent.rightChild.leftChild = root.centerRightChild;

                        root.parent.rightChild.rightChild = root.rightChild;

                        root.parent.rightChild.isLeaf = false;
                    }
                }
                addValue(value);
            }

        }

    }

    public boolean addToNode(int value) {
        // This is to add straight into a node (assumes splits have been done)
        if (root.isTwoNode()) {
            // 2 node simple search where should go and add
            if (root.value1 > value) {
                root.value2 = root.value1;
                root.value1 = value;

            } else if (root.value1 < value) {
                root.value2 = value;
            }
            root.values = 2;
            return true;
        } else if (root.isThreeNode()) {
            // Three node simple search and add
            if (root.value1 > value && root.value2 > value) {

                int temp1 = root.value1;
                root.value3 = root.value2;
                root.value2 = temp1;
                root.value1 = value;
            } else if (root.value1 < value && root.value2 > value) {

                root.value3 = root.value2;
                root.value2 = value;
            } else if (root.value1 < value && root.value2 < value) {

                root.value3 = value;
            }
            root.values = 3;
            return true;
        }
        return false;
    }

    public boolean addValue(int value) {
        // main brains for the function
        if (root == null) {
            // Create the root
            root = new TwoFourTreeItem(value);
        } else {

            if (root.isFourNode() || root.isLeaf == true) {
                // if the root is a leaf do this
                if (root.isFourNode() == false && root.parent == null) {
                    // add to root function super simple
                    addToNode(value);
                } else if (root.isFourNode() && root.isLeaf) {

                    // if the root is maxed out go here
                    twoNodeSplit(root, value);
                }
                // I had originally planned on doing this a different way
                // (hence the 2 different ifs that (i think) can be combined)
                else if (root.isFourNode() && root.isLeaf == false) {
                    // if we find the root being a 4 node we split it
                    twoNodeSplit(root, value);
                } else if (root.isLeaf) {

                    addToNode(value);
                }

            } else if (root.isTwoNode()) {
                TwoFourTreeItem temp = root;
                // This means root is a two node and only has left and right child
                if (root.value1 > value) {
                    root = root.leftChild;

                    addValue(value);

                } else if (root.value1 < value) {

                    root = root.rightChild;

                    addValue(value);

                }
                root = temp;

            } else if (root.isThreeNode()) {
                TwoFourTreeItem temp = root;
                if (root.value1 > value && root.value2 > value) {
                    // Smaller than both things in the root
                    root = root.leftChild;

                    addValue(value);
                } else if (root.value1 < value && root.value2 > value) {
                    // In the middle of both things in the root

                    root = root.centerChild;

                    addValue(value);
                } else if (root.value1 < value && root.value2 < value) {
                    // Larger than both things in the root

                    root = root.rightChild;

                    addValue(value);
                }
                root = temp;

            } else if (root.isFourNode()) {
                addValue(value);

            }

        }

        return false;
    }

    public boolean hasValue(int value)
    {
        return RhasValue(root,value);
    }
    public boolean RhasValue(TwoFourTreeItem root,int value) {
        // simple search

        if (root == null) {

            return false;
        }


        // hold the root so that we don't lose it
            if (root.isTwoNode()) {
            // recursive calls to search

            if (root.value1 == value) {
                return true;
            } else if (root.value1 > value) {
            
                return RhasValue(root.leftChild,value);
            } else if (root.value1 < value) {

                return RhasValue(root.rightChild,value);
            }
      

        } else if (root.isThreeNode()) {
            // more recursive calls to search

            if (root.value1 == value || root.value2 == value) {
                return true;
            } else if (root.value1 > value && root.value2 > value) {

                return RhasValue(root.leftChild,value);
            } else if (root.value1 < value && root.value2 > value) {

                return RhasValue(root.centerChild,value);
            } else if (root.value1 < value && root.value2 < value) {

                return RhasValue(root.rightChild,value);
            }

        } else if (root.isFourNode()) {
            // more recursive calls to search,
            // other functions don't have a 4 node case
            // This one does because theoretically it can be
   
            if (root.value1 == value || root.value2 == value || root.value3 == value) {
                return true;
            } else if (root.value1 > value && root.value2 > value && root.value3 > value) {

                return RhasValue(root.leftChild,value);
            } else if (root.value1 < value && root.value2 > value && root.value3 > value) {

                return RhasValue(root.centerLeftChild,value);
            } else if (root.value1 < value && root.value2 < value && root.value3 > value) {

                return RhasValue(root.centerRightChild,value);
            } else if (root.value1 < value && root.value2 < value && root.value3 < value) {

                return RhasValue(root.rightChild,value);
                
            }

        }

        return false;
    }

    public TwoFourTreeItem delTarget(TwoFourTreeItem root, int value) {
        // Simple function to find the delTarget, similar to hasValue()
        TwoFourTreeItem temp = root;
        if (temp.values == 1) {
            // Recursive searches for the value if 2node
            // we handle splits and rotations in successor search
            if (temp.value1 == value) {
                return temp;
            } else {
                if (temp.value1 > value) {
                    temp = temp.leftChild;
                    return delTarget(temp, value);
                } else if (temp.value1 < value) {
                    temp = temp.rightChild;
                    return delTarget(temp, value);
                }
            }
        } else if (temp.values == 2) {
            // Recursive search for 3 node
            if (temp.value1 == value || temp.value2 == value) {
                return temp;
            } else if (temp.value1 > value && temp.value2 > value) {

                temp = temp.leftChild;
                return delTarget(temp, value);
            } else if (temp.value1 > value && temp.value2 < value) {

                temp = temp.centerChild;
                return delTarget(temp, value);
            } else if (temp.value1 < value && temp.value2 < value) {

                temp = temp.rightChild;
                return delTarget(temp, value);
            }

        } else if (temp.values == 3) {
            // Recursive search for four node
            if (temp.value1 == value || temp.value2 == value || temp.value3 == value) {
                return temp;
            } else if (temp.value1 > value && temp.value2 > value && temp.value3 > value) {
                temp = temp.leftChild;
                return delTarget(temp, value);
            } else if (temp.value1 < value && temp.value2 > value && temp.value3 > value) {

                temp = temp.centerLeftChild;
                return delTarget(temp, value);
            } else if (temp.value1 < value && temp.value2 < value && temp.value3 > value) {

                temp = temp.centerRightChild;
                return delTarget(temp, value);
            } else if (temp.value1 < value && temp.value2 < value && temp.value3 < value) {
                temp = temp.rightChild;
                return delTarget(temp, value);
            }
        } else {
            return null;
        }

        return null;
    }

    public TwoFourTreeItem Successor(TwoFourTreeItem node, int value) {
        // This is where we search for right leftmost value
        // I think we should preform rotations and merges here only.
        if (node == null) {
            return null;
        }
        if (node.isLeaf) {
            return node;
        } else if (node.isLeaf == false) {
            // In this I do RM check
            // It's my method of figuring out whether I should rotate or merge on the way
            // down
            TwoFourTreeItem temp = node;
            if (temp.isTwoNode()) {
                // This is only accessible if root is a 2 node which we deal with in main delete
                // function
                temp = temp.rightChild;
                while (temp.leftChild != null) {

                    temp = temp.leftChild;
                }
            } else if (temp.isThreeNode()) {
                // rmCheck to determine whether we should rotate or merge or neither...
                // This is important to do everytime we traverse
                if (value == temp.value1) {
                    int ans = rmCheck(temp, temp.centerChild.value1);
                    if (ans == 1 || ans == 2) {
                        if (ans == 1) {
                            merge(temp);
                        } else {
                            rotate(temp, temp.centerChild.value1);
                        }
                    }
                    temp = temp.centerChild;

                    while (temp.leftChild != null) {
                        int ans2 = rmCheck(temp, value);
                        if (ans2 == 1 || ans2 == 2) {
                            if (ans2 == 1) {
                                merge(temp);
                            } else {
                                rotate(temp, value);
                            }
                        }
                        temp = temp.leftChild;
                    }
                } else if (value == temp.value2) {
                    int ans = rmCheck(temp, value);
                    if (ans == 1 || ans == 2) {
                        if (ans == 1) {
                            merge(temp);
                        } else {
                            rotate(temp, value);
                        }
                    }
                    temp = temp.rightChild;
                    while (temp.leftChild != null) {
                        int ans2 = rmCheck(temp, value);
                        if (ans2 == 1 || ans2 == 2) {
                            if (ans2 == 1) {
                                merge(temp);
                            } else {
                                rotate(temp, value);
                            }
                        }
                        temp = temp.leftChild;
                    }
                }
            } else if (temp.isFourNode()) {
                // rmCheck to determine whether we should rotate or merge or neither...
                // This is important to do everytime we traverse
                if (value == temp.value1) {
                    int ans = rmCheck(temp, value);
                    if (ans == 1 || ans == 2) {
                        if (ans == 1) {
                            merge(temp);
                        } else {
                            rotate(temp, value);
                        }
                    }
                    temp = temp.centerLeftChild;
                    while (temp.leftChild != null) {
                        int ans2 = rmCheck(temp, value);
                        if (ans2 == 1 || ans2 == 2) {
                            if (ans2 == 1) {
                                merge(temp);
                            } else {
                                rotate(temp, value);
                            }
                        }
                        temp = temp.leftChild;
                    }
                } else if (value == temp.value2) {
                    int ans = rmCheck(temp, value);
                    if (ans == 1 || ans == 2) {
                        if (ans == 1) {
                            merge(temp);
                        } else {
                            rotate(temp, value);
                        }
                    }
                    temp = temp.centerRightChild;
                    while (temp.leftChild != null) {
                        int ans2 = rmCheck(temp, value);
                        if (ans2 == 1 || ans2 == 2) {
                            if (ans2 == 1) {
                                merge(temp);
                            } else {
                                rotate(temp, value);
                            }
                        }
                        temp = temp.leftChild;
                    }
                } else if (value == temp.value3) {
                    int ans = rmCheck(temp, value);
                    if (ans == 1 || ans == 2) {
                        if (ans == 1) {
                            merge(temp);
                        } else {
                            rotate(temp, value);
                        }
                    }
                    temp = temp.rightChild;
                    while (temp.leftChild != null) {
                        int ans2 = rmCheck(temp, value);
                        if (ans2 == 1 || ans2 == 2) {
                            if (ans == 1) {
                                merge(temp);
                            } else {
                                rotate(temp, value);
                            }
                        }
                        temp = temp.leftChild;
                    }
                }
            }
            return temp;
        }

        return null;
    }

    public boolean merge(TwoFourTreeItem node) {
        // if we're in here both siblings are 2 nodes
        if (node.parent == null) {

        } else {
            if (node.isThreeNode()) {
                node.leftChild.values = 3;
                node.leftChild.value2 = node.value1;
                node.leftChild.value3 = node.centerChild.value1;
                node.value1 = node.value2;
                node.values = 1;
            }
        }

        return false;
    }

    public int rmCheck(TwoFourTreeItem node, int value) {
        // returns 1 for merge
        // 2 for rotate
        // Just check the childrens of whatever value we are passed through
        if (node.isTwoNode()) {
            if (node.leftChild.isTwoNode() && node.rightChild.isTwoNode()) {
                return 1;
            } else if (!node.leftChild.isTwoNode() || !node.rightChild.isTwoNode()) {
                return 2;
            } else {
                return 0;
            }
        } else if (node.isThreeNode()) {
            if (node.value1 > value) {
                if (node.leftChild.isTwoNode() && node.centerChild.isTwoNode()) {
                    return 1;
                } else if (node.leftChild.isTwoNode() && !node.centerChild.isTwoNode()) {
                    return 2;
                } else {
                    return 0;
                }
            } else if (node.value1 < value && node.value2 > value) {
                if (node.centerChild.isTwoNode() && node.rightChild.isTwoNode() && node.leftChild.isTwoNode()) {
                    return 1;
                } else if (node.centerChild.isTwoNode()
                        && (!node.rightChild.isTwoNode() || !node.leftChild.isTwoNode())) {
                    return 2;
                } else {
                    return 0;
                }
            } else if (node.value1 < value && node.value2 < value) {
                if (node.centerChild.isTwoNode() && node.rightChild.isTwoNode()) {
                    return 1;
                } else if (!node.centerChild.isTwoNode() && node.rightChild.isTwoNode()) {
                    return 2;
                } else {
                    return 0;
                }
            }
        } else if (node.isFourNode()) {
            if (node.value1 > value) {
                if (node.leftChild.isTwoNode() && node.centerLeftChild.isTwoNode()) {
                    return 1;
                } else if (node.leftChild.isTwoNode() && !node.centerLeftChild.isTwoNode()) {
                    return 2;
                } else {
                    return 0;
                }
            } else if (node.value1 < value && node.value2 > value) {
                if (node.leftChild.isTwoNode() && node.centerRightChild.isTwoNode()
                        && node.centerLeftChild.isTwoNode()) {
                    return 1;
                } else if (node.centerLeftChild.isTwoNode()
                        && (!node.centerRightChild.isTwoNode() || !node.leftChild.isTwoNode())) {
                    return 2;
                } else {
                    return 0;
                }
            } else if (node.value2 < value && node.value3 > value) {
                if (node.centerRightChild.isTwoNode() && node.rightChild.isTwoNode()
                        && node.centerLeftChild.isTwoNode()) {
                    return 1;
                } else if (node.centerRightChild.isTwoNode()
                        && !(node.rightChild.isTwoNode() || !node.centerLeftChild.isTwoNode())) {
                    return 2;
                } else {
                    return 0;
                }
            } else if (node.value3 < value) {
                if (node.centerRightChild.isTwoNode() && node.rightChild.isTwoNode()) {
                    return 1;
                } else if (node.rightChild.isTwoNode() && !node.centerRightChild.isTwoNode()) {
                    return 2;
                } else {
                    return 0;
                }
            }
        }

        return 0;
    }

    public boolean rotate(TwoFourTreeItem node, int value) {
        // Check what you got sent through
        if (node.isThreeNode()) {

            // we have to check what node we're going into,
            if (value > node.value1 && value > node.value2) {

                // We're going into the right node
                if (node.centerChild.isThreeNode()) {
                    // rotation if the only sibling is 3 node
                } else if (node.centerChild.isFourNode()) {
                    // rotation if the only sibling is 4 node
                }
            } else if (value > node.value1 && value < node.value2) {
                // we're going into the center node
                // check which sibling was the 3 or 4 node

                if (!node.leftChild.isTwoNode()) {
                    // we're going to rotate with the left child if it's not a 2 node
                    if (node.leftChild.isThreeNode()) {

                    } else if (node.leftChild.isFourNode()) {

                    }
                } else if (!node.rightChild.isTwoNode()) {
                    // we're going to rotate with the right child if above case failed,
                    if (node.rightChild.isThreeNode()) {
                        node.centerChild.values = 2;
                        node.centerChild.value2 = node.value2;
                        node.value2 = node.rightChild.value1;
                        node.rightChild.value1 = node.rightChild.value2;
                        node.rightChild.values = 1;

                    } else if (node.rightChild.isFourNode()) {
                        node.centerChild.value2 = node.value2;

                        node.centerChild.values = 2;
                        node.value2 = node.rightChild.value1;
                        node.rightChild.value1 = node.rightChild.value2;
                        node.rightChild.value2 = node.rightChild.value3;
                        node.centerChild.centerChild = node.centerChild.rightChild;
                        node.centerChild.centerChild.parent = node.centerChild;
                        node.centerChild.rightChild = node.rightChild.leftChild;
                        node.centerChild.rightChild.parent = node.centerChild;
                        node.rightChild.values = 2;
                        node.rightChild.leftChild = node.rightChild.centerLeftChild;
                        node.rightChild.centerChild = node.rightChild.centerRightChild;
                        node.values = 2;
                    }
                } else {
                    // what
                }
            } else if (value < node.value1 && value < node.value2) {

                // we're going into the left node
                if (node.centerChild.isThreeNode()) {
                    // rotation if the only sibling is 3 node
                } else if (node.centerChild.isFourNode()) {
                    // rotation if the only sibling is 4 node
                }
            }
        } else if (node.isFourNode()) {
            // I don't think we get here?
        }

        return false;
    }

    public boolean deleteValue(int value) {
        if (root == null) {
            return false;
        }
        TwoFourTreeItem delTarget = delTarget(root, value);
        if (delTarget == null) {
            root = null;
            return false;
        }
        if (delTarget.isLeaf && delTarget.parent == null) {
            // if current node is a root, with only 1 value we just make it null
            if (delTarget.values == 1) {
                root = null;
            } else if (delTarget.values == 2) {
                // if current node is a leaf is a root, with more than 2 values and no child
                if (value == delTarget.value2) {
                    // if the targets value is 2 delete it
                    delTarget.value2 = 0;
                    delTarget.values = 1;
                } else if (value == delTarget.value1) {
                    // if the target's value is 1 delete it
                    delTarget.value1 = delTarget.value2;
                    delTarget.values = 1;
                    delTarget.value2 = 0;
                } else {
                    // we shouldn't get here
                    return false;
                }
            } else if (delTarget.values == 3) {
                // if its a 3 value leaf root
                if (value == delTarget.value2) {
                    // if the targets value is 2 delete it
                    delTarget.value2 = 0;
                    delTarget.values = 2;
                    delTarget.value2 = delTarget.value3;
                } else if (value == delTarget.value1) {
                    // if the target's value is 1 delete it
                    delTarget.value1 = delTarget.value2;
                    delTarget.value2 = delTarget.value3;
                    delTarget.values = 2;
                } else if (value == delTarget.value3) {
                    delTarget.values = 2;
                } else {
                    return false;
                }
            }
        } else if (delTarget.isLeaf && delTarget.parent == null && delTarget.values == 1 && delTarget.leftChild != null && delTarget.centerChild == null) {
            // This is when the root is a single value with 2 children that are 2 nodes, we
            // just delete the value up top by merging in
            // This function could cause problems when the children are not leafs
            // I think this doesn't work when there are stuff within the left and right
            // values of the tree
            delTarget.values = 2;
            delTarget.value1 = delTarget.leftChild.value1;
            delTarget.value2 = delTarget.rightChild.value1;
            delTarget.leftChild = null;
            delTarget.rightChild = null;
        } else if (delTarget.isLeaf == false && delTarget.parent == null) {
            // This is the when the value is in the root, but we have children
            TwoFourTreeItem temp = Successor(delTarget, value);
            if (temp.values == 1) {
                delTarget.value1 = temp.value1;
                temp = null;
            } else if (temp.values == 2) {
                delTarget.value1 = temp.value2;
                temp.values = 1;
            } else if (temp.values == 3) {
                delTarget.value1 = temp.value3;
                temp.values = 2;
            }
        }
        // Everything above here is dealing with the root with no children
        else {
            // due to the nature of my other helper functions,
            // This is all that's left.
            if (delTarget.isThreeNode()) {
                TwoFourTreeItem n = Successor(delTarget, value);
                if (value == delTarget.value1 && n.isFourNode()) {
                    delTarget.value1 = n.value1;
                    n.values = 2;
                    n.value1 = n.value2;
                    n.value2 = n.value3;
                } else if (value == delTarget.value1 && n.isThreeNode()) {
                    delTarget.value1 = n.value1;
                    n.values = 1;
                    n.value1 = n.value2;
                }
            }
        }
        return false;

    }

    public void printInOrder() {
        if (root != null)
            root.printInOrder(0);
    }

    public TwoFourTree() {

    }
}
