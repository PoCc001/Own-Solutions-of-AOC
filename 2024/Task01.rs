use aoc_utils::file_to_string_vector;

fn find(pairs: &Vec<(i64, usize)>, v: i64, lb: usize) -> (usize, usize) {
    let mut result_times: usize = 0;
    let mut next_lb: usize = 0;
    for i in lb..pairs.len() {
        let (id, times) = pairs[i];
        if id == v {
            result_times = times;
            next_lb = i + 1;
            break;
        } else if id > v {
            next_lb = lb;
            break;
        }
    }
    (result_times, next_lb)
}

fn main() {
    let optional_input = file_to_string_vector("Input01.txt");
    if let Some(input) = optional_input {
        let mut list1: Vec<i64> = Vec::new();
        let mut list2: Vec<i64> = Vec::new();
        list1.resize(input.len(), 0);
        list2.resize(input.len(), 0);
        for (i, s) in input.iter().enumerate() {
            let split: Vec<&str> = s.split_ascii_whitespace().collect();
            list1[i] = split[0].parse().unwrap_or(0);
            if let Some(last) = split.last() {
                list2[i] = last.parse().unwrap_or(0);
            }
        }
        list1.sort();
        list2.sort();
        let mut diffs: i64 = 0;
        for i in 0..list1.len() {
            diffs += (list1[i] - list2[i]).abs();
        }
        println!("Part 1: {}", diffs);
        let mut paired_list1: Vec<(i64, usize)> = Vec::new();
        let mut paired_list2: Vec<(i64, usize)> = Vec::new();
        let mut prev1: usize = 0;
        let mut prev2: usize = 0;
        for i in 0..list1.len() {
            if list1[i] != list1[prev1] {
                paired_list1.push((list1[prev1], i - prev1));
                prev1 = i;
            }
            if list2[i] != list2[prev2] {
                paired_list2.push((list2[prev2], i - prev2));
                prev2 = i;
            }
        }
        paired_list1.push((list1[list1.len() - 1], list1.len() - prev1));
        paired_list2.push((list2[list2.len() - 1], list2.len() - prev2));
        let mut score: i64 = 0;
        let mut lower_bound: usize = 0;
        for i in 0..paired_list1.len() {
            let (id1, times1) = paired_list1[i];
            let (times2, next_lower_bound) = find(&paired_list2, id1, lower_bound);
            score += id1 * ((times1 * times2) as i64);
            lower_bound = next_lower_bound;
        }
        println!("Part 2: {}", score);
    }
}