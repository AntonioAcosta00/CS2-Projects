package SkipList;
import java.util.*;

public class SkipListSet<T extends Comparable<T>> implements SortedSet<T> {

// Skip List Class
public class SkipListSetItem<T> 
{
   int height;
   int size;
   T payload;
   SkipListSetItem<T> down;
   SkipListSetItem<T> up;
   SkipListSetItem<T> left;
   SkipListSetItem<T> right;

   public SkipListSetItem(T value, int height)
   {
        this.payload = value;
        this.height = height;
   }

   public SkipListSetItem()
   {
        this.payload = null;
        this.height = 1;
   }

}
    // Sets the head and headholder items
    private SkipListSetItem<T> head;
    private SkipListSetItem<T> headHold;
    private int size = 0;
    private int maxHeight = (int) (Math.log(size + 1) / Math.log(2));

    //default set constructor
    public SkipListSet()
    {
        head = new SkipListSetItem<>(null, 1);
        headHold = head;
        head.down = null;
    }
    
    class SortedSetIterator<T extends Comparable<T>> implements Iterator<T>{

    
        @Override
        public boolean hasNext() 
        {
            if(next()!= null) 
            {
                return true;
            }
            return false;
        }

        @Override
        public T next() 
        {

            return next();
        }

        @Override
        public void remove()
        {
            //I'm not quite sure what to put here
            return;
        }

    }
    
    //Gives us a random height determinded by the size, note this doesn't update the global max height
    public int randHeight() {
            
            int height = 1;
            int maxHeight = (int) (Math.log(size + 1) / Math.log(2));
            while (height < maxHeight && Math.random() < .5) 
            {
                height++;
            }
        
            return height;
    }
    
    

    public void reBalance()
    {
        //Stores everything into a array list
        List<SkipListSetItem<T>> payloadList = new ArrayList<>();

        //Traverse down to add
        SkipListSetItem<T> temp = head;
        while(temp.down != null)
        {
            temp = temp.down;
        }
        while(temp.right != null)
        {
            payloadList.add(temp.right);
            temp = temp.right;
        }
        //make a new skiplistset
        clear();
        //add them all again, effectively rebalancing the nodes.
        for (SkipListSetItem<T> item : payloadList) 
        {
            add(item.payload);    
        }
        
    }

    @Override
    public int size() 
    {

        return size;
    }

    @Override
    public boolean isEmpty() 
    {
        if(head.right == null)
        {
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) 
    {
 
            if (o == null)
            {
                return false;
            }
        

            T e = (T) o;
        
            SkipListSetItem<T> temp = head;
        
            //Traverse looking for item through traversal top bottom
            for (int i = maxHeight; i > 0; i--) 
            {
                while (temp.right != null && temp.right.payload.compareTo(e) <= 0) 
                {
                    temp = temp.right;
                }
                if (temp.payload != null && temp.payload.equals(e)) 
                {
                    return true;
                }
                if (temp.down != null) 
                {
                    temp = temp.down;
                }
            }
        
            return false; 
        
    }

    @Override
    public Iterator<T> iterator() 
    {
        
       return null;
    }

    @Override
    public Object[] toArray() 
    {
        Object[] arr = new Object[size];

        Iterator<T> iterator = iterator();
        int i = 0;

        while (iterator.hasNext()) 
        {
            arr[i] = iterator.next();
            i++;
        }
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) 
    {
        //My iterator wouldn't work here I don't know if that's on my end
        return null;
    }
    @Override
    public boolean add(T e) 
    {
        if(contains(e)) 
        {
            return false;
        }
        //Declare rand height
        int randHeight = randHeight();
        //This is one of my bugs
        if (maxHeight == 0) 
        {
            maxHeight = 1;
        }
        //If we have a new layer do this
        if (randHeight > maxHeight) 
        {
            int newLevels = randHeight - maxHeight;
            for (int i = 0; i < newLevels; i++) 
            {
                SkipListSetItem<T> newTopLevel = new SkipListSetItem<>(null, maxHeight + i + 1);
                newTopLevel.down = headHold;
                headHold.up = newTopLevel;
                headHold = newTopLevel;
            }
            maxHeight = randHeight;
            head = headHold;

        }
        
        SkipListSetItem<T> temp = head;
        SkipListSetItem<T> prevItem = null;
        boolean inserted = false;
        //Traverse only going in to insert if the height is = to i
        for (int i = head.height; i > 0; i--) 
        {
            SkipListSetItem<T> newItem = new SkipListSetItem<>(e, i);
            while (temp.right != null && temp.right.payload.compareTo(e) < 0) 
            {
                temp = temp.right;
            }
            if(i <= randHeight)
            {
                newItem.left = temp;
                newItem.right = temp.right;
                temp.right = newItem;
                if (newItem.right != null) 
                {
                    newItem.right.left = newItem;
                }
                if(i > 1)
                {
                    inserted = true;
                }
                if (prevItem != null && inserted == true) 
                {
                    prevItem.down = newItem;
                    newItem.up = prevItem;
                }
                prevItem = newItem;
            }
    

            if (temp.down != null)
            {
                temp = temp.down;
            } 
            else if (head.down != null) 
            {
                temp = head;
                head = head.down;
                temp = temp.down;
            }
        }
    
        inserted = false;
        head = headHold;
        size++;
        return true;
    }

    


    


    @Override
    public boolean remove(Object o) 
    {
        if (o == null) 
        {
            return false;
        }

        T e = (T) o;

        if (contains(e) == false) 
        {
            return false;
        }
     
        //Traversal
            SkipListSetItem<T> temp = head;
            for (int i = maxHeight; i > 0; i--) 
            {
                while (temp.right != null && temp.right.payload.compareTo(e) < 0) 
                {
                    temp = temp.right;
                }
                if (temp.down != null) 
                {
                    temp = temp.down;
                }
            }

            //Go back up to delete everything
            while (temp != null) 
            {
                if (temp.payload != null && temp.payload.equals(e)) 
                {

                    temp.left.right = temp.right;
                    if (temp.right != null) 
                    {
                        temp.right.left = temp.left;
                    }

                    temp = temp.down;
                } 
                else 
                {
                    temp = temp.right;
                }
            }
            size--;
            return true;
    }
    
    

    //Iterator to iterate through the list, and return whether contains is true;
    @Override
    public boolean containsAll(Collection<?> c) 
    {
        Iterator<?> iterator = c.iterator();
        while(iterator.hasNext()) 
        {
  
            if(contains(iterator.next()))
            {
                return true;
            }
        }
        return false;
    }
    

    //Use an iterator to iterate through the skiplist and add if it worked return.
    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean modified = false;
        

        Iterator<? extends T> iterator = c.iterator();

        while (iterator.hasNext()) 
        {
            T element = iterator.next();
            if (add(element)) 
            {
                modified = true;
            }
        }
    
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) 
    {
        // I don't quite know how to do this 
        return false;
    }



    //Use an iterator to remove each element and return if it worked.
    @Override
    public boolean removeAll(Collection<?> c) 
    {
        boolean modified = false;

        Iterator<?> iterator = c.iterator();
        while (iterator.hasNext())
         {
            if (remove(iterator.next())) 
            {
                modified = true;
            }
        }

        return modified;
    }


    @Override
    public void clear()
    {
        //Create a new empty skip list, and clear all the globals we have.
        head = new SkipListSetItem<>(null , 1);
        headHold = head;
        head.down = null;
        size = 0;
        maxHeight = (int) (Math.log(size + 1) / Math.log(2));
    }

    @Override
    public Comparator<? super T> comparator() 
    {

        return null;
    }

    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) 
    {

        throw new UnsupportedOperationException("nope");
    }

    @Override
    public SortedSet<T> headSet(T toElement) 
    {

        throw new UnsupportedOperationException("nope");
    }
    

    @Override
    public SortedSet<T> tailSet(T fromElement) 
    {
        throw new UnsupportedOperationException("nope");
    }

    @Override
    public T first() 
    {
        //just return the first value
        Iterator<T> iterator = iterator();

        if(iterator.hasNext())
        {
            return iterator.next();
        }
        return null;
    }

    @Override
    public T last() 
    {
        //go as far right and as far down and return the last value
        SkipListSetItem<T> temp = head;
        for (int i = maxHeight; i > 0; i--) 
                {
                    while (temp.right != null) 
                    {
                        temp = temp.right;
                    }
            
                    if (temp.down != null) 
                    {
                        temp = temp.down;
                    }
            }
        return temp.payload;
    }
    
}