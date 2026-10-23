
import 'package:flutter_test/flutter_test.dart';
import 'package:routinup_app/main.dart';

void main() {
  testWidgets('RoutinUP abre sem erros', (WidgetTester tester) async {
    await tester.pumpWidget(const RoutinUpApp());

    expect(find.byType(RoutinUpApp), findsOneWidget);
  });
}
