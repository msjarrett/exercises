use adventofcode2025::input;
use adventofcode2025::puzzle;

fn day_a(lines: &Vec<String>) -> u16 {
    let mut pos: i16 = 50;
    let mut zero_count: u16 = 0;

    for line in lines.iter() {
        let mut dist: i16 = line[1..].parse().unwrap();
        match line.chars().next().unwrap() {
            'L' => dist = -dist,
            'R' => {}
            other => panic!("Unexpected direction {}", other),
        }
        pos = (pos + dist).rem_euclid(100);
        if pos == 0 {
            zero_count += 1;
        }
        println!("Moved {} to {}", line, pos);
    }
    zero_count
}

#[allow(dead_code)]
fn calc_tick(pos: i16, dist: i16) -> (i16, u16) {
    let mut new_pos = pos + dist;
    let mut zero_count = 0;

    while new_pos >= 100 {
        new_pos -= 100;
        zero_count += 1;
    }
    while new_pos < 0 {
        new_pos += 100;
        zero_count += 1;
    }

    (new_pos, zero_count)
}

// Okay I'm embarassed to resort to this, but I just wanted to pass the day.
// I'll come back to this later.
fn dumb_calc_tick(pos: i16, dist: i16) -> (i16, u16) {
    let mut new_pos = pos;
    let mut zero_count = 0;

    let mut dist_left = dist;
    while dist_left != 0 {
        if dist_left > 0 {
            new_pos += 1;
            dist_left -= 1;
        } else {
            new_pos -= 1;
            dist_left += 1;
        }
        match new_pos {
            100 => {
                new_pos = 0;
                zero_count += 1;
            }
            0 => {
                zero_count += 1;
            }
            -1 => {
                new_pos = 99;
            }
            _ => {}
        }
    }
    (new_pos, zero_count)
}

// 6257 is wrong
// 6475 is right
fn day_b(lines: &Vec<String>) -> u16 {
    let mut pos: i16 = 50;
    let mut zero_count: u16 = 0;

    for line in lines.iter() {
        let mut dist: i16 = line[1..].parse().unwrap();
        match line.chars().next().unwrap() {
            'L' => dist = -dist,
            'R' => {}
            other => panic!("Unexpected direction {}", other),
        }
        let (new_pos, new_zeros) = dumb_calc_tick(pos, dist);
        pos = new_pos;
        zero_count += new_zeros;

        //println!("Moved {} ({}) to {}. Count is {}.", line, dist, pos, zero_count);
    }
    zero_count
}

fn main() {
    let lines = input::get_input_from_file("day1.txt");
    puzzle::run(day_a, day_b, &lines);
}

#[cfg(test)]
mod tests {
    use super::*;
    use adventofcode2025::puzzle::test;

    #[rustfmt::skip]
    const SAMPLE: &str =
"L68
L30
R48
L5
R60
L55
L1
L99
R14
L82";

    #[test]
    fn day_a_sample() {
        test::run_sample(day_a, SAMPLE, 3);
    }

    #[test]
    fn day_b_sample() {
        test::run_sample(day_b, SAMPLE, 6);
    }
}
