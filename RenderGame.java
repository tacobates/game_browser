//TODO: a class that extends a JPanel, contains a Game, and knows how to pack() itself
//TODO: this class can then be added to our GUI
//TODO: we'll make an array of them to use when paging.
  //TODO: if there aren't enough games to fill the page, this objects understands how to render itself when empty

//NOTE: DON'T just make Game extend JPanel for performance reasons
  //This way we only have a few GUI elements in memory, even though we have lots of lightweight Game objects
