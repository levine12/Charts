  $(function() {
    $( "#slider-range").slider({
      range: true,
      min: new Date('2010.01.01').getTime() / 1000,
      max: new Date().getTime() / 1000,
      step: 86400,
      values: [ new Date('2010.01.01').getTime() / 1000, new Date().getTime() / 1000 ],
      slide: function( event, ui ) {
        $( "#dates" ).val( (new Date(ui.values[ 0 ] *1000).toDateString() ) + " - " + (new Date(ui.values[ 1 ] *1000)).toDateString() );
      }
    });
 
    console.log((new Date($( "#slider-range").slider( "values", 0 )*1000).toDateString()) +
      " - " + (new Date($( "#slider-range").slider( "values", 1 )*1000)).toDateString());
    $( "#dates" ).val( (new Date($( "#slider-range").slider( "values", 0 )*1000).toDateString()) +
     " - " + (new Date($( "#slider-range").slider( "values", 1 )*1000)).toDateString());


  });
