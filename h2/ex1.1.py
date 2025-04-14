import csv
import random
import os

# This program is modified from our team's lab code.

def generate_student_id():
    return ''.join([str(random.randint(0, 9)) for _ in range(10)])

def generate_num():
    return random.randint(0, 100)

def generate_grade():
    return random.randint(0, 100)

if __name__ == '__main__':
    first_name_path = 'D:/downloads/data/firstnames.txt'
    last_name_path = 'D:/downloads/data/lastnames.txt'
    
    with open(first_name_path, 'r') as first_name_file, open(last_name_path, 'r') as last_name_file:
        first_names = [line.strip() for line in first_name_file]
        last_names = [line.strip() for line in last_name_file]
    
    students = [f"{first} {last}" for first in first_names for last in last_names]
    
    csv_data = []
    for student in students:
        student_id = generate_student_id()
        appear_times = generate_num()
        for _ in range(appear_times):
            grade = generate_grade()
            csv_data.append([student, student_id, grade])
    
    random.shuffle(csv_data)

    num_files = 1000
    file_count = 0
    records_per_file = len(csv_data) // num_files

    for i in range(num_files):
        file_data = csv_data[i * records_per_file: (i + 1) * records_per_file]
        if not file_data:
            break
        with open(f'students_{i + 1}.csv', 'w', encoding='utf8', newline='') as file:
            writer = csv.writer(file)
            writer.writerows(file_data)
        file_count += 1
    
    remaining_data = csv_data[num_files * records_per_file:]
    if remaining_data:
        with open(f'students_{file_count + 1}.csv', 'w', encoding='utf8', newline='') as file:
            writer = csv.writer(file)
            writer.writerows(remaining_data)
