// --made by bandinator#5428 (2023)
// --https://www.youtube.com/channel/UCdyMN5aRGibjSENUILp9J1Q
// --based on Firigion's & gnembon's similar scripts
// --https://github.com/gnembon/scarpet/blob/master/programs/utilities/flower.sc
// --https://github.com/Firigion/scarpets/blob/master/README.md#flowerify
//--fabric-carpet 1.4.93, fapric_api 0.73.0

//--no blue orchid as there's no point
global_flowers = [
  'dandelion',
  'poppy',
  'allium',
  'azure_bluet',
  'red_tulip',
  'orange_tulip',
  'white_tulip',
  'pink_tulip',
  'oxeye_daisy',
  'cornflower',
  'lily_of_the_valley'
];

global_glass = [
  'yellow_stained_glass',     // dandelion
  'brown_stained_glass',      // poppy
  'magenta_stained_glass',    // allium
  'light_gray_stained_glass', // azure_bluet
  'red_stained_glass',        // red_tulip
  'orange_stained_glass',     // orange_tulip
  'white_stained_glass',      // white_tulip
  'pink_stained_glass',       // pink_tulip
  'gray_stained_glass',       // oxeye_daisy
  'blue_stained_glass',       // cornflower
  'glass'                     // lily_of_the_valley
];

global_area_from = [0, 0, 0];
global_area_to = [0, 0, 0];
global_valid_selection = false;

__config() -> {
  'stay_loaded' -> true,
  'commands' -> {
    '' -> '_main_menu',
    'set_one_corner <one_corner>' -> 'set_one_corner',
    'set_other_corner <other_corner>' -> 'set_other_corner',
    'plant_flowers' -> 'plant_flowers',
    'add_glass' -> 'add_glass'
  },
  'arguments' -> {
    'from' -> {
      'type' -> 'pos'
    },
    'to' -> {
      'type' -> 'pos'
    },
    'one_corner' -> {
      'type' -> 'pos'
    },
    'other_corner' -> {
      'type' -> 'pos'
    }
  }
};

//--select the desired area's corners
set_one_corner(one_corner) -> (
    global_area_from = one_corner;
    print(format('i One corner of the selection is now set to ' + global_area_from))
);

set_other_corner(other_corner) -> (
    global_area_to = other_corner;
    print(format('i The other corner of the selection is now set to ' + global_area_to))
);

//--check if there's a selection and if it's valid (grass blocks)
check_selection() -> (
  if(global_area_from != [0, 0, 0]
    && global_area_to != [0, 0, 0]
    && block(global_area_from) == 'grass_block'
    && block(global_area_to) == 'grass_block',
    global_valid_selection = true
  )
);

//--notify the user about wrong selection
invalid_selection_message() -> (
  print(format('r There\'s no area selected or it\'s invalid! ', 'g To select an area use the set_<one|other>_corner commands! Each corner has to be a grass block!'))
);

//--clears grass duh
__clear_grass(loc) -> (
	scan(loc, l(3, 2, 3),
    if(
			block(_)=='grass' || block(_)=='tall_grass',
			  set(_, 'air')
		)
	);
);

//--this is the base of the whole thing, repeated 10 times (looped in plant_flowers)
__sweep(center, range) -> (
  scan(center,range,
		if(_ == 'grass_block',
			place_item('bone_meal', _);
			__clear_grass(_);
		)
	);
);

//--check the selection
//--if it's valid plant flowers
//--else notify the user
plant_flowers() ->(
  if(check_selection(),
    print(format('li Planting flowers...'));
    center = (global_area_from + global_area_to) / 2;
    range = center - global_area_from;
    loop(50, __sweep(center, range));
    print(format('li Done!')
  ),
    invalid_selection_message()
  )
);

//--check the selection
//--then fill layer above flowers with the corresponding glass type
add_glass() -> (
  if(check_selection(),
    print(format('gi Adding glass layer ...'));
    loop(length(global_flowers),
      current_flower = get(global_flowers, _);
      current_glass = get(global_glass, _);
      volume(global_area_from, global_area_to, if((block(_x, _y+1, _z) == current_flower), set(_x, _y+2, _z, current_glass)));
    );
    print(format('gi Done!')),
      invalid_selection_message()
  )
);

//--main menu, credits & how to
_main_menu() -> (
  menu_lines = [
    '',
    '+------------+',
    format('ie Flower Pattern Visualizer Scarpet App'),
    format('l Made by ', 'yu bandinator_', '@https://www.youtube.com/channel/UCdyMN5aRGibjSENUILp9J1Q', 'gi  (2023)'),

    format('w Based on ', 'yu Firigion\'s', '@https://github.com/Firigion/scarpets/blob/master/README.md#flowerify', 'w  and ', 'yu gnembon\'s', '@https://github.com/gnembon/scarpet/blob/master/programs/utilities/flower.sc', 'w  similar scripts.'),
    '+------------+',
    '',
    format('be How to use:'),
    format('yu Showcase video', '@https://www.youtube.com/watch?v=coming_soon'),
    format('b 1.', 'w  Select an area with the set corner commands. It has to be a flat grass_block surface with air above.'),
    format('b 2.', 'w  Plant flowers. The plant_flowers command does that 4 u ;)'),
    format('b 3.', 'w  (Optional) Run the add_glass command. It adds a layer of stained glass above the flowers based on their type. This makes everything nicer to look at, basically the whole reason why I made this.'),
    format('b 4.', 'w  Enjoy the pretty landscape you just made ☺'),
    '',
    format('br Commands:'),
    format('y /flower_pattern_visual', 'w  - main command, shows this page'),
    '',
    format('y /flower_pattern_visual set_one_corner <x> <y> <z>', 'w  - pick a block for one corner of the area where you\'d like to have flowers'),
    '',
    format('y /flower_pattern_visual set_other_corner <x> <y> <z>', 'w  - pick a block for the other corner of the area where you\'d like to have flowers'),
    '',
    format('y /flower_pattern_visual plant_flowers ', 'w  - plants flowers on the selected area specified by the set_corner commands (Tries to plant a flower 10 times on every block, just rerun the command if there are holes/not enough flowers)'),
    '',
    format('y /flower_pattern_visual add_glass ', 'w  - adds a layer of stained glass above the flowers for better visibility, based on the flower type (optional)')
  ];

  p = player();
  for(menu_lines, print(p, _));

);
