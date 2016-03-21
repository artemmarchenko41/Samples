# Expandble RecyclerView
Sample how to use RecyclerView like expandble list with selected items.

![](img.gif)



###Using

```java
private void initPlaceTypeMenu(){
	mPlaceTypeRecyclerView = (RecyclerView) findViewById(R.id.place_type_recycler_view);
	mPlaceTypeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
	mPlaceTypeRecyclerView.setAdapter(new PlaceTypesAdapter(prepareData(this)));
}

//TEST DATA
private List<PlaceType> prepareData(final Context context) {
	List<PlaceType> data = new ArrayList<>();
        data.add(new PlaceType(context, "hospital",
                new ArrayList<PlaceType>() {{
                    add(new PlaceType(context, "aaa"));
                    add(new PlaceType(context, "bbb"));
                }}));

        data.add(new PlaceType(context, "atm", null));
        data.add(new PlaceType(context, "hotel", null));
        return data;
 }
```