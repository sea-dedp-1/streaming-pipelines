import json
import time
from argparse import ArgumentParser


def update_last_updated_timestamp_to_now(file_path: str) -> dict:
    with open(file_path, "r") as fp:
        data = json.load(fp)

        return dict(data, last_updated=int(time.time()))


def write_updated_data(updated_data: dict, output_file_path: str):
    with open(output_file_path, "w") as fp:
        json.dump(updated_data, fp, indent=2)


if __name__ == "__main__":
    parser = ArgumentParser()

    parser.add_argument("template_json_file", help="JSON file template to be used")
    parser.add_argument("output_path", help="File path to write updated data")

    args = parser.parse_args()

    updated_data = update_last_updated_timestamp_to_now(args.template_json_file)

    write_updated_data(
        updated_data=updated_data,
        output_file_path=args.output_path,
    )
