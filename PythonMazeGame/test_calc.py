import unittest
import Helper

class TestHelper(unittest.TestCase):

    def test_touching_obstical(self):
        num = 1
        x = 268
        y = 480
        result = Helper.touching_obstical(num, x, y)
        self.assertEqual(result,False)
