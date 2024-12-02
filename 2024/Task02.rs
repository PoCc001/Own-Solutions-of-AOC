use aoc_utils::file_to_string_vector;

fn main() {
    let optional_input = file_to_string_vector("Input02.txt");
    if let Some(input) = optional_input {
        let mut matrix: Vec<Vec<i64>> = Vec::new();
        for s in input {
            let split = s.split_ascii_whitespace();
            matrix.push(split.map(|n| n.parse().unwrap_or(0)).collect());
        }
        let mut safe_map: Vec<bool> = Vec::new();
        let mut safe_counter = 0;
        for a in &matrix {
            let mut incr = false;
            if a.len() >= 2 {
                if a[0] == a[1] {
                    safe_map.push(false);
                    continue;
                } else {
                    incr = a[0] < a[1];
                    if (a[1] - a[0]).abs() > 3 {
                        safe_map.push(false);
                        continue;
                    }
                }
            } else {
                safe_counter += 1;
                safe_map.push(true);
            }
            let mut is_safe = true;
            for i in 2..a.len() {
                if incr {
                    if a[i - 1] >= a[i] || a[i] - a[i - 1] > 3 {
                        is_safe = false;
                    }
                } else {
                    if a[i - 1] <= a[i] || a[i - 1] - a[i] > 3 {
                        is_safe = false;
                    }
                }

                if !is_safe {
                    break;
                }
            }
            if is_safe {
                safe_counter += 1;
            }
            safe_map.push(is_safe);
        }
        println!("Part 1: {safe_counter}");
        
        for (i, a) in matrix.iter().enumerate() {
            if !safe_map[i] {
                let mut is_safe = true;
                for j in 0..a.len() {
                    is_safe = true;
                    let mut a_removed_one: Vec<i64> = Vec::new();
                    for k in 0..a.len() {
                        if j != k {
                            a_removed_one.push(a[k]);
                        }
                    }
                    let mut incr = false;
                    if a_removed_one.len() >= 2 {
                        if a_removed_one[0] == a_removed_one[1] {
                            is_safe = false;
                        } else {
                            incr = a_removed_one[0] < a_removed_one[1];
                            if (a_removed_one[1] - a_removed_one[0]).abs() > 3 {
                                is_safe = false;
                            }
                        }
                    } else {
                        safe_counter += 1;
                        safe_map.push(true);
                    }
                    for i in 2..a_removed_one.len() {
                        if incr {
                            if a_removed_one[i - 1] >= a_removed_one[i] || a_removed_one[i] - a_removed_one[i - 1] > 3 {
                                is_safe = false;
                            }
                        } else {
                            if a_removed_one[i - 1] <= a_removed_one[i] || a_removed_one[i - 1] - a_removed_one[i] > 3 {
                                is_safe = false;
                            }
                        }

                        if !is_safe {
                            break;
                        }
                    }
                    if is_safe {
                        break;
                    }
                }
                if is_safe {
                    safe_counter += 1;
                }
            }
        }
        println!("Part 2: {safe_counter}");
    }
}
