use adventofcode2025::puzzle;
use std::cmp::max;

const YEAR: u16 = 2025;
const DAY: u8 = 5;

#[allow(dead_code)]
#[rustfmt::skip]
const SAMPLE: &str =
"3-5
10-14
16-20
12-18

1
5
8
11
17
32";

fn day_a(lines: &Vec<String>) -> u64 {
    let mut is_ids = false;
    let mut fresh_ranges: Vec<(u64, u64)> = Vec::new();
    let mut ids: Vec<u64> = Vec::new();

    for i in 0..lines.len() {
        if lines[i].is_empty() {
            assert!(!is_ids);
            is_ids = true;
        } else if !is_ids {
            let strs: Vec<&str> = lines[i].split("-").collect();
            assert_eq!(strs.len(), 2);
            fresh_ranges.push((strs[0].parse().unwrap(), strs[1].parse().unwrap()));
        } else {
            ids.push(lines[i].parse().unwrap());
        }
    }

    let mut fresh_ids = 0;

    // TODO: Sort the ranges and bisect.
    for id in ids {
        for range in &fresh_ranges {
            if id >= range.0 && id <= range.1 {
                fresh_ids += 1;
                break;
            }
        }
    }

    fresh_ids
}

fn day_b(lines: &Vec<String>) -> u64 {
    let mut ranges: Vec<(u64, u64)> = Vec::new();

    for i in 0..lines.len() {
        if lines[i].is_empty() {
            break;
        } else {
            let strs: Vec<&str> = lines[i].split("-").collect();
            assert_eq!(strs.len(), 2);
            ranges.push((strs[0].parse().unwrap(), strs[1].parse().unwrap()));
        }
    }

    ranges.sort_by_key(|a| a.0);
    
    // We're mutating the list so have to iterate manually.
    let mut i = 1;
    while i < ranges.len() {
        if ranges[i].0 <= ranges[i - 1].1 {
            let new_end = max(ranges[i - 1].1, ranges[i].1);
            ranges[i - 1].1 = new_end;

            // Remove could be expensive. If this is too slow, we can just nully the element out, but will
            // have to track the index of the "last valid" range.
            ranges.remove(i);
        } else {
            i += 1
        }
    }

    // Count the ranges.
    let mut fresh_ids = 0;
    for range in ranges {
        fresh_ids += range.1 - range.0 + 1;
    }
    fresh_ids
}

fn main() {
    puzzle::run(day_a, day_b, YEAR, DAY);
}

#[cfg(test)]
mod tests {
    use super::*;
    use adventofcode2025::puzzle::test;

    #[test]
    fn day5_a_sample() {
        test::run_sample(day_a, SAMPLE, 3);
    }

    #[test]
    fn day5_b_sample() {
        test::run_sample(day_b, SAMPLE, 14);
    }

    #[test]
    fn day5_a_input() {
        test::run_input(day_a, YEAR, DAY, 652);
    }

    #[test]
    fn day5_b_input() {
        test::run_input(day_b, YEAR, DAY, 341753674214273);
    }
}
