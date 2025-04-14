import os
import random


def generate_random_bytes(size):
    return bytearray(random.getrandbits(8) for _ in range(size))


def generate_small_binary_files(output_dir, num_files, file_size):
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)

    for i in range(1, num_files + 1):
        file_name = f"file_{i}.bin"
        file_path = os.path.join(output_dir, file_name)

        with open(file_path, 'wb') as f:
            data = generate_random_bytes(file_size)
            f.write(data)

        print(f"Generated file: {file_path}")


if __name__ == "__main__":
    output_dir = "D:/472/h2/ex4/demo/binaryFiles"
    num_files = 5000  # 生成5000个文件
    file_size = 256  # 每个文件包含256字节

    generate_small_binary_files(output_dir, num_files, file_size)
