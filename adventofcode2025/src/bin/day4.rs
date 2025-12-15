use adventofcode2025::parse::Grid;
use adventofcode2025::puzzle;

const YEAR: u16 = 2025;
const DAY: u8 = 4;

#[allow(dead_code)]
#[rustfmt::skip]
const SAMPLE: &str =
"..@@.@@@@.
 @@@.@.@.@@
 @@@@@.@.@@
 @.@@@@..@.
 @@.@@@@.@@
 .@@@@@@@.@
 .@.@.@.@@@
 @.@@@.@@@@
 .@@@@@@@@.
 @.@.@@@.@.";

fn day_a(lines: &Vec<String>) -> u32 {
    let grid = Grid::from_lines(lines).unwrap();
    let mut rolls: u32 = 0;

    for (x, y, item) in grid.iter() {
        if item != '@' {
            continue;
        }
        let mut n_adj = 0;
        for (_, _, item_adj) in grid.iter_adjacent(x, y) {
            if item_adj == '@' {
                n_adj += 1;
            }
        }
        if n_adj < 4 {
            rolls += 1;
        }
    }

    rolls
}

fn day_b(lines: &Vec<String>) -> u32 {
    let mut grid = Grid::from_lines(lines).unwrap();
    let mut rolls: u32 = 0;

    loop {
        let mut removed_rolls: Vec<(usize, usize)> = Vec::new();

        for (x, y, item) in grid.iter() {
            if item != '@' {
                continue;
            }
            let mut n_adj = 0;
            for (_, _, item_adj) in grid.iter_adjacent(x, y) {
                if item_adj == '@' {
                    n_adj += 1;
                }
            }
            if n_adj < 4 {
                removed_rolls.push((x, y));
            }
        }

        if removed_rolls.is_empty() {
            break;
        }
        rolls += removed_rolls.len() as u32;

        for (x, y) in removed_rolls {
            grid.set(x, y, 'X');
        }
    }

    rolls
}

fn main() {
    puzzle::run(day_a, day_b, YEAR, DAY);
}

#[cfg(test)]
mod tests {
    use super::*;
    use adventofcode2025::puzzle::test;

    #[test]
    fn day4_a_sample() {
        test::run_sample(day_a, SAMPLE, 13);
    }

    #[test]
    fn day4_b_sample() {
        test::run_sample(day_b, SAMPLE, 43);
    }

    #[test]
    fn day4_a_input() {
        test::run_input(day_a, YEAR, DAY, 1516);
    }

    #[test]
    fn day4_b_input() {
        test::run_input(day_b, YEAR, DAY, 9122);
    }
}
